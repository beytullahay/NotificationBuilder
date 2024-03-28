package com.example.notificationbuilder

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationbuilder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIF_ID = 0

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotifChannel()

        val intent=Intent(this,MainActivity::class.java)

        // pendingIntent bildirime tıklandığında aktiviteye geri dönmesini sağlıyor
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // Bildirim oluşturuluyor
        val notif = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Test Bildirim Başlığı")
            .setContentText("Bu bildirim test amaçlı atılmıştır.")
            .setSmallIcon(R.drawable.ic_baseline_info_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // pendingIntent burada çağırılıyor
            .build()

        // Bildirim yöneticisi oluşturuluyor
        val notifManger = NotificationManagerCompat.from(this)

        // Butona basıldığında bildirimi göstermesi ayarlanıyor
        binding.btnShowNotif.setOnClickListener {
            notifManger.notify(NOTIF_ID,notif)
        }

    }

    // Bildirim kanalı oluşturma
    private fun createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}