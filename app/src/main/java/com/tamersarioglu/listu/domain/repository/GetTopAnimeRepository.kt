package com.tamersarioglu.listu.domain.repository

import com.tamersarioglu.listu.domain.model.topanimemodel.TopAnimePage

interface GetTopAnimeRepository {
    suspend fun getTopAnime(
        type: String? = null,
        filter: String? = null,
        rating: String? = null,
        sfw: Boolean = true,
        page: Int = 1,
        limit: Int = 25
    ): Result<TopAnimePage>
}