package com.android.course.stepcounter.di

import com.android.course.stepcounter.present.MainActivityBroadcastReceiver
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideBroadcastReceiver(): MainActivityBroadcastReceiver = MainActivityBroadcastReceiver()
}