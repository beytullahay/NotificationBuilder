package com.example.notificationbuilder

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationbuilder.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIF_ID = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotifChannel()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // Butona basıldığında sendNotification'a title ve text gönderiliyor
        binding.btnShowNotif.setOnClickListener {
            val title = "Test Bildirimi"
            val text = "Bu Bildirim Test Amaçlı Gönderilmiştir."
            sendNotification(title, text, pendingIntent)
        }
    }

    // Bildirim kanalı oluşturma
    private fun createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    // Bildirim Gönderme Fonksiyonu constructor'ında title, text ve pendingIntent istiyor.
    private fun sendNotification(title: String, text: String, pendingIntent: PendingIntent) {
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_baseline_info_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)  // pendingIntent burada çağırılıyor
            .build()

        // Bildirim yöneticisi oluşturuluyor
        val notifManager = NotificationManagerCompat.from(this)

        // kontrol işlemi yapılıyor
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notifManager.notify(NOTIF_ID, notif)
    }
}