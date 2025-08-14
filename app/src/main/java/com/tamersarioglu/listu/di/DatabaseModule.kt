package com.tamersarioglu.listu.di

import android.content.Context
import androidx.room.Room
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

    companion object {
        @Provides
        @Singleton
        fun provideListuDatabase(@ApplicationContext context: Context): ListuDatabase {
            return Room.databaseBuilder(
                context,
                ListuDatabase::class.java,
                ListuDatabase.DATABASE_NAME
            ).build()
        }

        @Provides
        fun provideFavoriteAnimeDao(database: ListuDatabase): FavoriteAnimeDao {
            return database.favoriteAnimeDao()
        }
    }
}