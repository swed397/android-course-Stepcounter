package com.android.course.stepcounter.present

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.course.stepcounter.PARAM_STEP_EXTRA
import com.android.course.stepcounter.STEP_ACTION

class MainActivityBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == STEP_ACTION) {
            Log.d("TEST", "THIS IS HEEEEEE")
            val value = intent.getFloatExtra(PARAM_STEP_EXTRA, 0.0f)
            Log.d("TEST", value.toString())


        }
        Log.d("TEST", " RECEIVER RECEIVE!!!!")
    }
}