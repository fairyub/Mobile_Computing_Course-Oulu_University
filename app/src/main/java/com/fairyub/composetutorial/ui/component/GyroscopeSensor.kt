package com.fairyub.composetutorial.ui.component

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import com.fairyub.composetutorial.R
import androidx.core.content.ContextCompat.getSystemService
import kotlin.math.sqrt
import android.util.Half.EPSILON
import androidx.compose.ui.platform.LocalContext
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class GyroscopeSensorForegroundService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var gyro: Sensor? = null

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()

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
}