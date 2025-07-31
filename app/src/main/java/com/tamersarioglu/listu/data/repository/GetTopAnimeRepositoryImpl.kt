package com.tamersarioglu.listu.data.repository

import com.tamersarioglu.listu.data.remote.api.topanimeservice.TopAnimeService
import com.tamersarioglu.listu.data.remote.mapper.topanimemapper.toDomain
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.repository.GetTopAnimeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTopAnimeRepositoryImpl @Inject constructor(
    private val topAnimeService: TopAnimeService
) : GetTopAnimeRepository {

    override suspend fun getTopAnime(
        type: String?,
        filter: String?,
        rating: String?,
        sfw: Boolean,
        page: Int,
        limit: Int
    ): Result<List<Anime>> {
        return try {
            val response = topAnimeService.getTopAnime(type, filter, rating, sfw, page, limit)
            val animeList = response.data.map { it.toDomain() }
            Result.success(animeList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}