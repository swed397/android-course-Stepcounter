package com.android.course.stepcounter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.course.stepcounter.databinding.ActivityMainBinding
import kotlin.math.abs
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sensorManager: SensorManager
    private var oldValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkPermission()
        init()
    }

    override fun onResume() {
        super.onResume()
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (stepSensor != null) {
            sensorManager.registerListener(
                object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent?) {
                        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]

                            val magnitude = sqrt(x * x + y * y + z * z)
                            if (abs(oldValue) - abs(magnitude) >= ACCURACY) {
                                val newValue = getCurrentSteps() + 1
                                binding.stepsTextView.text = newValue.toString()
                                binding.progressBar.progress = newValue.toFloat()
                            }
                            oldValue = magnitude.toInt()
                        }
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    }
                },
                stepSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        } else {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onStop() {
        saveSteps()
        super.onStop()
    }

    private fun init() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val stepsFromPref = sharedPreferences.getInt(STEPS_COUNTER_KEY_NAME, -1)
        binding.stepsTextView.text = stepsFromPref.toString()
    }

    private fun checkPermission() {
        if (applicationContext.checkSelfPermission(
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                1
            );
        }
    }

    private fun saveSteps() {
        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(STEPS_COUNTER_KEY_NAME, getCurrentSteps())
        editor.apply()
    }

    private fun getCurrentSteps(): Int = Integer.parseInt(binding.stepsTextView.text.toString())

    private companion object {
        const val ACCURACY = 2
        const val SHARED_PREF_NAME = "StepCounterPref"
        const val STEPS_COUNTER_KEY_NAME = "StepCounter"
    }
}