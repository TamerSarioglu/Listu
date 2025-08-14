package com.tamersarioglu.listu.domain.repository

import com.tamersarioglu.listu.domain.model.topanimemodel.TopAnimePage

interface SearchAnimeRepository {
    suspend fun searchAnime(
        query: String? = null,
        unapproved: Boolean? = null,
        page: Int? = null,
        limit: Int? = null,
        type: String? = null,
        score: Double? = null,
        minScore: Double? = null,
        maxScore: Double? = null,
        status: String? = null,
        rating: String? = null,
        sfw: Boolean? = null,
        genres: String? = null,
        genresExclude: String? = null,
        orderBy: String? = null,
        sort: String? = null,
        letter: String? = null,
        producers: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Result<TopAnimePage>
}