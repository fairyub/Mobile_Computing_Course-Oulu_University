package com.fairyub.composetutorial.ui.component

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import com.fairyub.composetutorial.R
import androidx.core.content.ContextCompat.getSystemService
import kotlin.math.sqrt
import android.util.Half.EPSILON
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class GyroscopeSensorForegroundService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var gyro: Sensor? = null

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        startInForeground()

        createNotificationChannel(this)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (gyro == null) {
            createNotification(this, "No gyro", "This device/emulator has no gyroscope")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        gyro?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSensorChanged(event: SensorEvent) {
        val axisY = event.values[1]

        val threshold = 0.2f

        if (abs(axisY) > threshold) {
            createNotification(
                context = this,
                title = "Gyro",
                message = "Whoa, spinning ${"%.2f".format(axisY)}"
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun startInForeground() {
        val channelId = "gyro_channel"

        // tạo notification channel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Gyro Service",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Gyroscope running")
            .setContentText("Tracking sensor in background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }
}