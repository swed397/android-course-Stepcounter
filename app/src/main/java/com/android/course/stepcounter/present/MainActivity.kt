package com.android.course.stepcounter.present

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
import com.android.course.stepcounter.R
import com.android.course.stepcounter.data.PrefRepo
import com.android.course.stepcounter.databinding.ActivityMainBinding
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sensorManager: SensorManager
    private var oldValue = 0

//    @Inject
//    private lateinit var prefRepo: PrefRepo

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
                            if (abs(oldValue) - abs(magnitude) >= applicationContext.resources.getInteger(
                                    R.integer.accuracy
                                )
                            ) {
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
//        prefRepo.save(getCurrentSteps())
        super.onStop()
    }

    private fun init() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        binding.stepsTextView.text = prefRepo.get().toString()
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

    private fun getCurrentSteps(): Int = Integer.parseInt(binding.stepsTextView.text.toString())
}