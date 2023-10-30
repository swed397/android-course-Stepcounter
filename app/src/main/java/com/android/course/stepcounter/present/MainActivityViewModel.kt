package com.android.course.stepcounter.present

import androidx.lifecycle.ViewModel
import com.android.course.stepcounter.domain.StepCounterPrefRepo
import javax.inject.Inject
import kotlin.math.abs

class MainActivityViewModel @Inject constructor(private val repo: StepCounterPrefRepo) :
    ViewModel() {

    private var oldValue = 0

    fun save(steps: Int) = repo.save(steps)

    fun getSteps(): Int = repo.get()

    fun detectSteps(currentStepsValue: Float, accuracy: Int): Boolean {
        var isStep = false
        if (abs(oldValue - currentStepsValue) >= accuracy) {
            isStep = true
        }
        oldValue = currentStepsValue.toInt()
        return isStep
    }
}