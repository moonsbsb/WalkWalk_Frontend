package com.example.pet_walk

import com.example.pet_walk.data.MessageRepository
import com.example.pet_walk.data.Repository.MessageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository
}