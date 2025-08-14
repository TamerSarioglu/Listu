package com.tamersarioglu.listu.domain.usecase

import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    operator fun invoke(): Flow<List<Anime>> = favoritesRepository.getAllFavorites()
}