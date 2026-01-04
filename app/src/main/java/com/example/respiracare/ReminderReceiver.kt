package com.example.respiracare

import android.Manifest
import android.app.*
import android.content.*
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Log.d("REMINDER_TEST", "ALARM MASUK 🔔")

        val nama = intent.getStringExtra("NAMA") ?: "Obat"
        val id = intent.getStringExtra("ID") ?: return

        val notifIntent = Intent(context, Notifikasi::class.java).apply {
            putExtra("ID", id)
        }


        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notifIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "obat_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pengingat Obat",
                NotificationManager.IMPORTANCE_HIGH
            )
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notif = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.medicine)
            .setContentTitle("Waktunya Minum Obat")
            .setContentText(nama)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)

        // ✅ INI YANG DIMINTA ERROR ANDROID STUDIO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("REMINDER_TEST", "Permission notifikasi BELUM diizinkan")
                return
            }
        }

        notificationManager.notify(id.hashCode(), notif)
    }
}