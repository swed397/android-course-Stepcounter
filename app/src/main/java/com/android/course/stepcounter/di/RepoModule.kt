package com.android.course.stepcounter.di

import android.content.Context
import com.android.course.stepcounter.data.PrefRepo
import dagger.Module
import dagger.Provides

@Module
class RepoModule(private val context: Context) {

    @Provides
    fun providePrefRepo(): PrefRepo = PrefRepo(context)
}