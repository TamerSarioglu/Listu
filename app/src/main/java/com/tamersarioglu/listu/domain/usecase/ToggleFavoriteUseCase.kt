package com.tamersarioglu.listu.domain.usecase

import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(anime: Anime): Boolean {
        return favoritesRepository.toggleFavorite(anime)
    }
}

class IsFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(malId: Int): Boolean {
        return favoritesRepository.isFavorite(malId)
    }

    fun asFlow(malId: Int): Flow<Boolean> {
        return favoritesRepository.isFavoriteFlow(malId)
    }
}