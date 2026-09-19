package com.example.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.ui.theme.QuinceBlush
import com.example.ui.theme.QuinceRose
import java.io.File

@Composable
fun MemoryImage(
    imageUri: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val imageModel = remember(imageUri) {
        resolveImageModel(context, imageUri)
    }

    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageModel)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun resolveImageModel(context: Context, uriString: String): Any {
    if (uriString.startsWith("res://drawable/")) {
        val resName = uriString.removePrefix("res://drawable/")
        return when (resName) {
            "quince_hero_banner" -> R.drawable.quince_hero_banner
            "quince_vals_moment" -> R.drawable.quince_vals_moment
            "quince_cake_festive" -> R.drawable.quince_cake_festive
            "ic_quince_tiara" -> R.drawable.ic_quince_tiara
            else -> {
                val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
                if (resId != 0) resId else R.drawable.quince_hero_banner
            }
        }
    }
    if (uriString.startsWith("/") || uriString.startsWith("file://")) {
        val cleanPath = uriString.removePrefix("file://")
        val file = File(cleanPath)
        if (file.exists()) {
            return file
        }
    }
    return uriString
}
