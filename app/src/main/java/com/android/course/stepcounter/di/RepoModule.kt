package com.android.course.stepcounter.di

import android.content.Context
import android.content.SharedPreferences
import com.android.course.stepcounter.data.StepCounterPrefRepoImpl
import com.android.course.stepcounter.domain.StepCounterPrefRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepoModule.BindModule::class])
class RepoModule(private val context: Context) {

    @Provides
    @Singleton
    fun providePref(): SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    @Module
    interface BindModule {

        @Binds
        @Singleton
        fun bindRepo(impl: StepCounterPrefRepoImpl): StepCounterPrefRepo
    }


    private companion object {
        const val SHARED_PREF_NAME = "StepCounterPref"
    }
}