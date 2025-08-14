package com.tamersarioglu.listu.data.repository

import com.tamersarioglu.listu.data.remote.api.searchanimeservice.SearchAnimeService
import com.tamersarioglu.listu.data.remote.mapper.topanimemapper.toDomain
import com.tamersarioglu.listu.domain.model.topanimemodel.TopAnimePage
import com.tamersarioglu.listu.domain.repository.SearchAnimeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchAnimeRepositoryImpl @Inject constructor(
    private val searchAnimeService: SearchAnimeService
) : SearchAnimeRepository {
    override suspend fun searchAnime(
        query: String?,
        unapproved: Boolean?,
        page: Int?,
        limit: Int?,
        type: String?,
        score: Double?,
        minScore: Double?,
        maxScore: Double?,
        status: String?,
        rating: String?,
        sfw: Boolean?,
        genres: String?,
        genresExclude: String?,
        orderBy: String?,
        sort: String?,
        letter: String?,
        producers: String?,
        startDate: String?,
        endDate: String?
    ): Result<TopAnimePage> {
        return try {
            val response = searchAnimeService.searchAnime(
                query, unapproved, page, limit, type, score, minScore, maxScore,
                status, rating, sfw, genres, genresExclude, orderBy, sort,
                letter, producers, startDate, endDate
            )
            val items = response.data.map { it.toDomain() }
            Result.success(
                TopAnimePage(
                    currentPage = response.pagination.currentPage,
                    hasNextPage = response.pagination.hasNextPage,
                    items = items
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}