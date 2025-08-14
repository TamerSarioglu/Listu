package com.tamersarioglu.listu.di

import android.content.Context
import androidx.room.Room
import com.tamersarioglu.listu.core.network.ConnectivityObserver
import com.tamersarioglu.listu.core.network.NetworkConnectivityObserver
import com.tamersarioglu.listu.data.local.dao.AnimeDao
import com.tamersarioglu.listu.data.local.dao.FavoriteAnimeDao
import com.tamersarioglu.listu.data.local.database.ListuDatabase
import com.tamersarioglu.listu.data.repository.FavoritesRepositoryImpl
import com.tamersarioglu.listu.domain.repository.FavoritesRepository
import dagger.Module
import dagger.Provides
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository

    @Binds
    abstract fun bindConnectivityObserver(
        networkConnectivityObserver: NetworkConnectivityObserver
    ): ConnectivityObserver

    companion object {
        @Provides
        @Singleton
        fun provideListuDatabase(@ApplicationContext context: Context): ListuDatabase {
            return Room.databaseBuilder(
                context,
                ListuDatabase::class.java,
                ListuDatabase.DATABASE_NAME
            )
                .fallbackToDestructiveMigration(false) // For now, since we're in development
                .build()
        }

        @Provides
        fun provideFavoriteAnimeDao(database: ListuDatabase): FavoriteAnimeDao {
            return database.favoriteAnimeDao()
        }

        @Provides
        fun provideAnimeDao(database: ListuDatabase): AnimeDao {
            return database.animeDao()
        }
    }
}