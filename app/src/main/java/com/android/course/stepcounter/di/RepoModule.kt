package com.android.course.stepcounter.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class RepoModule(private val context: Context) {

    @Provides
    fun provideSharedPref(): SharedPreferences =
        context.getSharedPreferences("StepCounterPref", Context.MODE_PRIVATE)
}