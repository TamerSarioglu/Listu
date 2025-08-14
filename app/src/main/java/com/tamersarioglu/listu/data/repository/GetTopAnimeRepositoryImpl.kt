package com.tamersarioglu.listu.data.repository

import com.tamersarioglu.listu.core.network.ConnectivityObserver
import com.tamersarioglu.listu.data.local.dao.AnimeDao
import com.tamersarioglu.listu.data.local.mapper.toDomainList
import com.tamersarioglu.listu.data.local.mapper.toEntityList
import com.tamersarioglu.listu.data.remote.api.topanimeservice.TopAnimeService
import com.tamersarioglu.listu.data.remote.mapper.topanimemapper.toDomain
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.model.topanimemodel.TopAnimePage
import com.tamersarioglu.listu.domain.repository.GetTopAnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTopAnimeRepositoryImpl @Inject constructor(
    private val topAnimeService: TopAnimeService,
    private val animeDao: AnimeDao,
    private val connectivityObserver: ConnectivityObserver
) : GetTopAnimeRepository {

    companion object {
        private const val CACHE_TYPE_TOP = "top_anime"
        private const val CACHE_DURATION_HOURS = 24
        private const val CACHE_DURATION_MS = CACHE_DURATION_HOURS * 60 * 60 * 1000L
    }

    override suspend fun getTopAnime(
        type: String?,
        filter: String?,
        rating: String?,
        sfw: Boolean,
        page: Int,
        limit: Int
    ): Result<TopAnimePage> {

        val isConnected = connectivityObserver.isConnected()
        val lastCacheTime = animeDao.getLastCacheTime(CACHE_TYPE_TOP) ?: 0
        (System.currentTimeMillis() - lastCacheTime) < CACHE_DURATION_MS

        val cachedData = animeDao.getCachedAnimePage(CACHE_TYPE_TOP, page)

        return try {
            when {
                (!isConnected && cachedData.isNotEmpty()) -> {
                    Result.success(
                        TopAnimePage(
                            items = cachedData.toDomainList(),
                            currentPage = page,
                            hasNextPage = true // Assume there might be more pages
                        )
                    )
                }

                // Try network call if online
                isConnected -> {
                    try {
                        val response = topAnimeService.getTopAnime(
                            type = type,
                            filter = filter,
                            rating = rating,
                            page = page,
                            limit = limit
                        )

                        val animeList = response.data.map { it.toDomain() }
                        val topAnimePage = TopAnimePage(
                            items = animeList,
                            currentPage = page,
                            hasNextPage = response.pagination.hasNextPage
                        )

                        // Cache the successful response
                        if (animeList.isNotEmpty()) {
                            animeDao.insertAnimeList(
                                animeList.toEntityList(CACHE_TYPE_TOP, page)
                            )
                        }

                        Result.success(topAnimePage)
                    } catch (e: Exception) {
                        // Fallback to cache on network error
                        if (cachedData.isNotEmpty()) {
                            Result.success(
                                TopAnimePage(
                                    items = cachedData.toDomainList(),
                                    currentPage = page,
                                    hasNextPage = true
                                )
                            )
                        } else {
                            Result.failure(e)
                        }
                    }
                }

                // No data available (offline and no cache)
                else -> {
                    Result.success(
                        TopAnimePage(
                            items = emptyList(),
                            currentPage = page,
                            hasNextPage = false
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCachedTopAnime(): Flow<List<Anime>> {
        return animeDao.getCachedAnime(CACHE_TYPE_TOP).map { entities ->
            entities.toDomainList()
        }
    }

    suspend fun clearCache() {
        animeDao.clearCacheByType(CACHE_TYPE_TOP)
    }

    suspend fun clearOldCache() {
        val cutoffTime = System.currentTimeMillis() - CACHE_DURATION_MS
        animeDao.clearOldCache(cutoffTime)
    }
}