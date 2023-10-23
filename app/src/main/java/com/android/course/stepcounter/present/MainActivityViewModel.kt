package com.android.course.stepcounter.present

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.lifecycle.ViewModel
import com.android.course.stepcounter.domain.StepCounterPrefRepo
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.sqrt

class MainActivityViewModel @Inject constructor(private val repo: StepCounterPrefRepo) :
    ViewModel() {

    private var oldValue = 0

    fun save(steps: Int) = repo.save(steps)

    fun getSteps(): Int = repo.get()

    fun detectSteps(
        currentSteps: Int,
        accuracy: Int,
        event: SensorEvent?,
        onBind: (newValue: Int) -> Unit
    ) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val magnitude = sqrt(x * x + y * y + z * z)
            if (abs(oldValue - magnitude) >= accuracy) {
                onBind.invoke(currentSteps + 1)
            }
            oldValue = magnitude.toInt()
        }
    }
}