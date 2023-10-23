package com.android.course.stepcounter.data

import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import com.android.course.stepcounter.domain.StepCounterPrefRepo
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.sqrt

class StepCounterPrefRepoImpl @Inject constructor(private val preferences: SharedPreferences) :
    StepCounterPrefRepo {

    private var oldValue = 0

    override fun save(steps: Int) {
        val editor = preferences.edit()
        editor.putInt(STEPS_COUNTER_KEY_NAME, steps)
        editor.apply()
    }

    override fun get(): Int = preferences.getInt(STEPS_COUNTER_KEY_NAME, -1)
    override fun detectSteps(event: SensorEvent?, accuracy: Int) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val magnitude = sqrt(x * x + y * y + z * z)
            if (abs(oldValue) - abs(magnitude) >= accuracy
            ) {
            }
        }
    }

    private companion object {
        const val STEPS_COUNTER_KEY_NAME = "StepCounter"
    }
}