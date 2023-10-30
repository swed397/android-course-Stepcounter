package com.android.course.stepcounter.present

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.android.course.stepcounter.R
import com.android.course.stepcounter.STEP_ACTION
import com.android.course.stepcounter.STOP_ACTION
import com.android.course.stepcounter.databinding.ActivityMainBinding
import com.android.course.stepcounter.di.App
import com.android.course.stepcounter.domain.StepCounterPrefRepo
import com.android.course.stepcounter.domain.UiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private val mainActivityViewModel: MainActivityViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var stepCounterPrefRepo: StepCounterPrefRepo

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var broadcastReceiver: MainActivityBroadcastReceiver


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkPermission()

        init()
        startForegroundService(Intent(this, MainActivityService::class.java))
        registerReceiver()
    }

    private fun init() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        binding.stepsTextView.text = mainActivityViewModel.getSteps().toString()
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(STOP_ACTION)
            addAction(STEP_ACTION)
        }

        broadcastReceiver.stepValueFlow
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                val isStep = mainActivityViewModel.detectSteps(
                    it, resources.getInteger(R.integer.accuracy)
                )
                if (isStep) binding.stepsTextView.text = (getCurrentSteps() + 1).toString()
            }
            .launchIn(lifecycleScope)

        broadcastReceiver.uiState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { if (it is UiState.OnStop) finish() }
            .launchIn(lifecycleScope)

        registerReceiver(broadcastReceiver, intentFilter)
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

    override fun onDestroy() {
        mainActivityViewModel.save(getCurrentSteps())
        broadcastReceiver.close()
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun getCurrentSteps(): Int = Integer.parseInt(binding.stepsTextView.text.toString())
}