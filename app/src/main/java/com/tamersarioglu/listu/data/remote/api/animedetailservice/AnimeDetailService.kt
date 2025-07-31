package com.tamersarioglu.listu.data.remote.api.animedetailservice

import com.tamersarioglu.listu.data.remote.dto.animedetaildto.AnimeDetailResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeDetailService {
    @GET("anime/{mal_id}/full")
    suspend fun getAnimeDetail(
        @Path("mal_id") malId: Int
    ): AnimeDetailResponseDto
}