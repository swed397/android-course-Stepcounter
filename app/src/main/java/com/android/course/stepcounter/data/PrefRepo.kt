package com.android.course.stepcounter.data

import android.content.Context
import javax.inject.Inject

class PrefRepo constructor(context: Context) {

    private val preference by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun save(steps: Int) {
        val editor = preference.edit()
        editor.putInt(STEPS_COUNTER_KEY_NAME, steps)
        editor.apply()
    }

    fun get(): Int = preference.getInt(STEPS_COUNTER_KEY_NAME, -1)

    fun detectSteps() {

    }

    private companion object {
        const val ACCURACY = 2
        const val SHARED_PREF_NAME = "StepCounterPref"
        const val STEPS_COUNTER_KEY_NAME = "StepCounter"
    }
}