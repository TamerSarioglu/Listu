package com.tamersarioglu.listu.di

import com.tamersarioglu.listu.data.repository.GetTopAnimeRepositoryImpl
import com.tamersarioglu.listu.domain.repository.GetTopAnimeRepository
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
    abstract fun bindGetTopAnimeRepository(
        getTopAnimeRepositoryImpl: GetTopAnimeRepositoryImpl
    ): GetTopAnimeRepository
}