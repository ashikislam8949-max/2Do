package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationHelper {
  private const val CHANNEL_ID = "bbci_gov_alerts_channel"
  private const val CHANNEL_NAME = "2Do Tech Government & Iqama Alerts"
  private const val CHANNEL_DESC = "Push notifications for Iqama expiry, service updates, and government portal announcements"

  fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val importance = NotificationManager.IMPORTANCE_HIGH
      val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
        description = CHANNEL_DESC
      }
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun sendPushNotification(context: Context, title: String, message: String, notificationId: Int = 1001) {
    createNotificationChannel(context)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
        return
      }
    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setContentTitle(title)
      .setContentText(message)
      .setStyle(NotificationCompat.BigTextStyle().bigText(message))
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setAutoCancel(true)

    try {
      with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
      }
    } catch (e: SecurityException) {
      e.printStackTrace()
    }
  }
}
