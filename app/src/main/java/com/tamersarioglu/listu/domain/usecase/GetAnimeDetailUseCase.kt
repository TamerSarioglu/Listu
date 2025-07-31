package com.tamersarioglu.listu.domain.usecase

import com.tamersarioglu.listu.domain.model.animedetailmodel.AnimeDetail
import com.tamersarioglu.listu.domain.repository.GetAnimeDetailRepository
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(
    private val repository: GetAnimeDetailRepository
) {
    suspend operator fun invoke(malId: Int): Result<AnimeDetail> {
        return repository.getAnimeDetail(malId)
    }
}