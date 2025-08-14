package com.tamersarioglu.listu.data.local.dao

import androidx.room.*
import com.tamersarioglu.listu.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Query("SELECT * FROM cached_anime WHERE cacheType = :cacheType ORDER BY `rank` ASC")
    fun getCachedAnime(cacheType: String): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM cached_anime WHERE cacheType = :cacheType AND page = :page ORDER BY `rank` ASC")
    suspend fun getCachedAnimePage(cacheType: String, page: Int): List<AnimeEntity>

    @Query("SELECT * FROM cached_anime WHERE malId = :malId LIMIT 1")
    suspend fun getAnimeById(malId: Int): AnimeEntity?

    @Query("SELECT * FROM cached_anime WHERE title LIKE '%' || :query || '%' OR titleEnglish LIKE '%' || :query || '%' ORDER BY score DESC")
    fun searchCachedAnime(query: String): Flow<List<AnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)

    @Update
    suspend fun updateAnime(anime: AnimeEntity)

    @Delete
    suspend fun deleteAnime(anime: AnimeEntity)

    @Query("DELETE FROM cached_anime WHERE cacheType = :cacheType")
    suspend fun clearCacheByType(cacheType: String)

    @Query("DELETE FROM cached_anime WHERE cacheTimestamp < :timestamp")
    suspend fun clearOldCache(timestamp: Long)

    @Query("DELETE FROM cached_anime")
    suspend fun clearAllCache()

    @Query("SELECT COUNT(*) FROM cached_anime WHERE cacheType = :cacheType")
    suspend fun getCacheCount(cacheType: String): Int

    @Query("SELECT MAX(cacheTimestamp) FROM cached_anime WHERE cacheType = :cacheType")
    suspend fun getLastCacheTime(cacheType: String): Long?

    @Query("SELECT DISTINCT page FROM cached_anime WHERE cacheType = :cacheType ORDER BY page")
    suspend fun getCachedPages(cacheType: String): List<Int>
}