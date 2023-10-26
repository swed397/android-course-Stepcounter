package com.android.course.stepcounter.di

import com.android.course.stepcounter.present.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RepoModule::class, ViewModelModule::class, AppModule::class])
@Singleton
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}