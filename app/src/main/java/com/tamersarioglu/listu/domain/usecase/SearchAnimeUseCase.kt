package com.tamersarioglu.listu.domain.usecase

import com.tamersarioglu.listu.domain.model.topanimemodel.TopAnimePage
import com.tamersarioglu.listu.domain.repository.SearchAnimeRepository
import javax.inject.Inject

class SearchAnimeUseCase @Inject constructor(
    private val repository: SearchAnimeRepository
) {
    suspend operator fun invoke(
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
    ): Result<TopAnimePage> {
        return repository.searchAnime(
            query, unapproved, page, limit, type, score, minScore, maxScore,
            status, rating, sfw, genres, genresExclude, orderBy, sort,
            letter, producers, startDate, endDate
        )
    }
}