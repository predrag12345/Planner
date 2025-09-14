package com.example.planner.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePlansRepository(firestore: FirebaseFirestore): PlansRepository {
        return PlansRepositoryImpl(firestore)
    }



}
