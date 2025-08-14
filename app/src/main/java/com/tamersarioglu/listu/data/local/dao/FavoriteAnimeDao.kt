package com.tamersarioglu.listu.data.local.dao

import androidx.room.*
import com.tamersarioglu.listu.data.local.entity.FavoriteAnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAnimeDao {

    @Query("SELECT * FROM favorite_anime ORDER BY dateAdded DESC")
    fun getAllFavorites(): Flow<List<FavoriteAnimeEntity>>

    @Query("SELECT * FROM favorite_anime WHERE malId = :malId")
    suspend fun getFavoriteById(malId: Int): FavoriteAnimeEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_anime WHERE malId = :malId)")
    suspend fun isFavorite(malId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_anime WHERE malId = :malId)")
    fun isFavoriteFlow(malId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteAnime: FavoriteAnimeEntity)

    @Delete
    suspend fun deleteFavorite(favoriteAnime: FavoriteAnimeEntity)

    @Query("DELETE FROM favorite_anime WHERE malId = :malId")
    suspend fun deleteFavoriteById(malId: Int)

    @Query("DELETE FROM favorite_anime")
    suspend fun deleteAllFavorites()

    @Query("SELECT COUNT(*) FROM favorite_anime")
    suspend fun getFavoriteCount(): Int
}