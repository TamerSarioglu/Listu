package com.tamersarioglu.listu.data.remote.api.searchanimeservice

import com.tamersarioglu.listu.data.remote.dto.common.AnimeListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchAnimeService {
    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String? = null,
        @Query("unapproved") unapproved: Boolean? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("type") type: String? = null,
        @Query("score") score: Double? = null,
        @Query("min_score") minScore: Double? = null,
        @Query("max_score") maxScore: Double? = null,
        @Query("status") status: String? = null,
        @Query("rating") rating: String? = null,
        @Query("sfw") sfw: Boolean? = null,
        @Query("genres") genres: String? = null,
        @Query("genres_exclude") genresExclude: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("letter") letter: String? = null,
        @Query("producers") producers: String? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null
    ): AnimeListResponseDto
}