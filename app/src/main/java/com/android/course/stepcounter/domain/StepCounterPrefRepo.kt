package com.android.course.stepcounter.domain

import android.hardware.SensorEvent

interface StepCounterPrefRepo {

    fun save(steps: Int)

    fun get(): Int

    fun detectSteps(event: SensorEvent?, accuracy: Int)
}