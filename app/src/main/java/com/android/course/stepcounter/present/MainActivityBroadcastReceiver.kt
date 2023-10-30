package com.android.course.stepcounter.present

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.course.stepcounter.PARAM_STEP_EXTRA
import com.android.course.stepcounter.STEP_ACTION
import com.android.course.stepcounter.STOP_ACTION
import com.android.course.stepcounter.domain.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.Closeable

class MainActivityBroadcastReceiver : BroadcastReceiver(), Closeable {

    private val _stepValueFlow = MutableSharedFlow<Float>(
        replay = 0,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val stepValueFlow: Flow<Float>
        get() = _stepValueFlow
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())

    private val _uiState = MutableStateFlow<UiState>(UiState.OnWork)
    val uiState: StateFlow<UiState>
        get() = _uiState


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == STEP_ACTION) {
            val value = intent.getFloatExtra(PARAM_STEP_EXTRA, 0.0f)
            scope.launch { _stepValueFlow.emit(value) }
        }
        if (intent?.action == STOP_ACTION) {
            scope.launch { _uiState.emit(UiState.OnStop) }
        }
    }

    override fun close() {
        scope.cancel()
    }
}