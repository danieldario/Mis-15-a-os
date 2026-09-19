package com.example.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.data.local.MemoryDao
import com.example.data.model.MemoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

enum class ReactionType {
    HEART, CHEERS, SPARKLES
}

class MemoryRepository(
    private val memoryDao: MemoryDao,
    private val context: Context
) {
    val allMemories: Flow<List<MemoryEntity>> = memoryDao.getAllMemories()

    fun getMemoriesByTag(tag: String): Flow<List<MemoryEntity>> {
        return if (tag.equals("Todos", ignoreCase = true)) {
            memoryDao.getAllMemories()
        } else {
            memoryDao.getMemoriesByTag(tag)
        }
    }

    suspend fun addMemory(memory: MemoryEntity): Long = withContext(Dispatchers.IO) {
        memoryDao.insertMemory(memory)
    }

    suspend fun react(id: Long, type: ReactionType) = withContext(Dispatchers.IO) {
        when (type) {
            ReactionType.HEART -> memoryDao.incrementHearts(id)
            ReactionType.CHEERS -> memoryDao.incrementCheers(id)
            ReactionType.SPARKLES -> memoryDao.incrementSparkles(id)
        }
    }

    suspend fun vote(id: Long) = withContext(Dispatchers.IO) {
        memoryDao.incrementVotes(id)
    }

    suspend fun unvote(id: Long) = withContext(Dispatchers.IO) {
        memoryDao.decrementVotes(id)
    }

    suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        memoryDao.deleteMemory(id)
    }

    /**
     * Client-side image optimization:
     * Resizes the image to max 1280px on longest edge and compresses to JPEG ~82% quality,
     * saving bandwidth, memory, and disk space.
     */
    suspend fun optimizeAndSaveImage(sourceUri: Uri): String = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver
            var inputStream: InputStream? = contentResolver.openInputStream(sourceUri)

            // Decode bounds only
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            val maxDimension = 1280
            val originalWidth = options.outWidth
            val originalHeight = options.outHeight

            var sampleSize = 1
            if (originalHeight > maxDimension || originalWidth > maxDimension) {
                val halfHeight = originalHeight / 2
                val halfWidth = originalWidth / 2
                while ((halfHeight / sampleSize) >= maxDimension && (halfWidth / sampleSize) >= maxDimension) {
                    sampleSize *= 2
                }
            }

            // Decode bitmap with inSampleSize
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
            }
            inputStream = contentResolver.openInputStream(sourceUri)
            val decodedBitmap = BitmapFactory.decodeStream(inputStream, null, decodeOptions)
            inputStream?.close()

            if (decodedBitmap == null) {
                return@withContext sourceUri.toString()
            }

            // Scale down if still larger than maxDimension
            val ratio = decodedBitmap.width.toFloat() / decodedBitmap.height.toFloat()
            val targetWidth: Int
            val targetHeight: Int
            if (decodedBitmap.width > decodedBitmap.height) {
                targetWidth = minOf(decodedBitmap.width, maxDimension)
                targetHeight = (targetWidth / ratio).toInt()
            } else {
                targetHeight = minOf(decodedBitmap.height, maxDimension)
                targetWidth = (targetHeight * ratio).toInt()
            }

            val scaledBitmap = if (targetWidth != decodedBitmap.width || targetHeight != decodedBitmap.height) {
                Bitmap.createScaledBitmap(decodedBitmap, targetWidth, targetHeight, true)
            } else {
                decodedBitmap
            }

            // Save to app internal memories directory
            val memoriesDir = File(context.filesDir, "quince_memories").apply { mkdirs() }
            val outputFile = File(memoriesDir, "memory_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}.jpg")
            FileOutputStream(outputFile).use { out ->
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 82, out)
            }

            if (scaledBitmap != decodedBitmap) {
                scaledBitmap.recycle()
            }
            decodedBitmap.recycle()

            outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            sourceUri.toString()
        }
    }
}
