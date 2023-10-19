package com.android.course.stepcounter.di

import com.android.course.stepcounter.present.MainActivity
import dagger.Component

@Component(modules = [RepoModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}