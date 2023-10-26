package com.android.course.stepcounter.di

import com.android.course.stepcounter.present.MainActivityBroadcastReceiver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun getBroadcastReceiver(): MainActivityBroadcastReceiver = MainActivityBroadcastReceiver()
}