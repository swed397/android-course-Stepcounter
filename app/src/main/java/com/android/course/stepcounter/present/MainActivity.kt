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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.course.stepcounter.R
import com.android.course.stepcounter.databinding.ActivityMainBinding
import com.android.course.stepcounter.di.App
import com.android.course.stepcounter.domain.StepCounterPrefRepo
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private val mainActivityViewModel: MainActivityViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var stepCounterPrefRepo: StepCounterPrefRepo

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)

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
                        val accuracy = applicationContext.resources.getInteger(R.integer.accuracy)
                        mainActivityViewModel.detectSteps(getCurrentSteps(), accuracy, event)
                        { newValue ->
                            binding.stepsTextView.text = newValue.toString()
                            binding.progressBar.progress = newValue.toFloat()
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
        mainActivityViewModel.save(getCurrentSteps())
        super.onStop()
    }

    private fun init() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        binding.stepsTextView.text = mainActivityViewModel.getSteps().toString()
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