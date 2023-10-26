package com.android.course.stepcounter.domain

interface BroadcastReceiverListener {

    fun getValue(stepValue: Float)
}