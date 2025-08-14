package com.tamersarioglu.listu.domain.repository

import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getAllFavorites(): Flow<List<Anime>>
    suspend fun getFavoriteById(malId: Int): Anime?
    suspend fun isFavorite(malId: Int): Boolean
    fun isFavoriteFlow(malId: Int): Flow<Boolean>
    suspend fun addToFavorites(anime: Anime)
    suspend fun removeFromFavorites(malId: Int)
    suspend fun toggleFavorite(anime: Anime): Boolean
    suspend fun clearAllFavorites()
    suspend fun getFavoriteCount(): Int
}