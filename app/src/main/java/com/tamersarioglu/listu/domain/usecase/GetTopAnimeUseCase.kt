package com.tamersarioglu.listu.domain.usecase

import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.repository.GetTopAnimeRepository
import javax.inject.Inject

class GetTopAnimeUseCase @Inject constructor(
    private val repository: GetTopAnimeRepository
) {
    suspend operator fun invoke(
        type: String? = null,
        filter: String? = null,
        rating: String? = null,
        sfw: Boolean = true,
        page: Int = 1,
        limit: Int = 25
    ): Result<List<Anime>> {
        return repository.getTopAnime(type, filter, rating, sfw, page, limit)
    }
}