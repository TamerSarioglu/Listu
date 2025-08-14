package com.tamersarioglu.listu.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tamersarioglu.listu.data.local.dao.AnimeDao
import com.tamersarioglu.listu.data.local.dao.FavoriteAnimeDao
import com.tamersarioglu.listu.data.local.entity.AnimeEntity
import com.tamersarioglu.listu.data.local.entity.FavoriteAnimeEntity

@Database(
    entities = [
        FavoriteAnimeEntity::class,
        AnimeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ListuDatabase : RoomDatabase() {
    abstract fun favoriteAnimeDao(): FavoriteAnimeDao
    abstract fun animeDao(): AnimeDao

    companion object {
        const val DATABASE_NAME = "listu_database"
    }
}