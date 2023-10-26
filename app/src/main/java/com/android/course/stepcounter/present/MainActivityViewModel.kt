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

    fun detectSteps(
        currentSteps: Int,
        accuracy: Int,
    ) {
        //ToDo refactor
        if (abs(oldValue - currentSteps) >= accuracy) {
//            onBind.invoke(currentSteps + 1)
        }
        oldValue = currentSteps
    }
}