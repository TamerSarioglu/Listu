package com.tamersarioglu.listu.data.remote.api.topanimeservice

import com.tamersarioglu.listu.data.remote.dto.common.AnimeListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface TopAnimeService {
    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("type") type: String? = null,
        @Query("filter") filter: String? = null,
        @Query("rating") rating: String? = null,
        @Query("sfw") sfw: Boolean = true,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): AnimeListResponseDto
}