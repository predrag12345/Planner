package com.example.planner.di

import android.content.Context
import com.example.planner.data.localDataSource.LocalDataSource
import com.example.planner.data.localDataSource.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideLocalDataSource(@ApplicationContext context: Context): LocalDataSource {
        return SharedPref(context)
    }



}