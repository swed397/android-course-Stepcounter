package com.android.course.stepcounter.domain

sealed class UiState {

     data object OnWork : UiState()
     data object OnStop : UiState()
}