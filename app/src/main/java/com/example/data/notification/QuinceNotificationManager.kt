package com.example.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.MemoryEntity

object QuinceNotificationManager {
    const val CHANNEL_ID = "quince_uploads_channel"
    private const val CHANNEL_NAME = "Nuevos Recuerdos y Dedicatorias"
    private const val CHANNEL_DESC = "Alertas en tiempo real cuando un invitado sube una foto o dedicatoria para Luchy"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun notifyNewMemoryUploaded(context: Context, memory: MemoryEntity) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "👑 ¡Nuevo recuerdo para Luchy!"
        val snippet = if (memory.dedication.isNotBlank()) {
            "\"${memory.dedication}\""
        } else {
            "Foto compartida en categoría ${memory.tag}"
        }
        val contentText = "${memory.guestName} (${memory.tableNumber}): $snippet"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_quince_tiara)
            .setContentTitle(title)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            val notificationId = (memory.id.toInt().takeIf { it != 0 } ?: (System.currentTimeMillis() % 10000).toInt())
            notificationManager.notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Permission not granted or restricted
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
