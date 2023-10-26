package com.android.course.stepcounter.present

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.android.course.stepcounter.PARAM_STEP_EXTRA
import com.android.course.stepcounter.R
import com.android.course.stepcounter.STEP_ACTION
import kotlin.math.sqrt


class MainActivityService : Service() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.flags == PendingIntent.FLAG_CANCEL_CURRENT) {
            stopForeground(STOP_FOREGROUND_DETACH)
            stopSelf()
        }

        createSensorManager()
        val notification = createNotification()
        ServiceCompat.startForeground(this, 100, notification, 0)
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotification(): Notification {
        val chan =
            NotificationChannel(CHANEL_ID, "Notification", NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = Color.CYAN
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan);

        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, PendingIntent.FLAG_UPDATE_CURRENT, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(applicationContext, this::class.java)
        val stopPendingIntent =
            PendingIntent.getService(
                applicationContext,
                0,
                stopIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        return NotificationCompat.Builder(this, CHANEL_ID)
            .setContentTitle(resources.getString(R.string.step_counter_notification_title))
            .setContentText(resources.getString(R.string.step_counter_notification_content))
            .setColor(Color.BLUE)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(Notification.CATEGORY_EVENT)
            .setSmallIcon(R.drawable.notification_step_counter_icon)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.notification_stop_icon,
                resources.getString(R.string.stop_service_text),
                stopPendingIntent
            )
            .build()
    }

    private fun createSensorManager() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (stepSensor != null) {
            sensorManager.registerListener(
                object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent?) {
                        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                            val value = detectStepByEvent(event)
                            val sendIntent = Intent(STEP_ACTION).apply {
                                putExtra(PARAM_STEP_EXTRA, value)
                            }
                            sendBroadcast(sendIntent)
                        }
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    }
                },
                stepSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun detectStepByEvent(event: SensorEvent): Float {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        return sqrt(x * x + y * y + z * z)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null

    }

    private companion object {
        const val CHANEL_ID = "CHANEL_ID"
    }
}