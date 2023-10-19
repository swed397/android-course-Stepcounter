package com.android.course.stepcounter.di

import android.app.Application

class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent =
            DaggerAppComponent.builder().repoModule(RepoModule(this.applicationContext)).build()
    }
}