package com.tamersarioglu.listu.data.repository

import com.tamersarioglu.listu.data.local.dao.FavoriteAnimeDao
import com.tamersarioglu.listu.data.local.mapper.toDomain
import com.tamersarioglu.listu.data.local.mapper.toFavoriteEntity
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoriteAnimeDao: FavoriteAnimeDao
) : FavoritesRepository {

    override fun getAllFavorites(): Flow<List<Anime>> {
        return favoriteAnimeDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getFavoriteById(malId: Int): Anime? {
        return favoriteAnimeDao.getFavoriteById(malId)?.toDomain()
    }

    override suspend fun isFavorite(malId: Int): Boolean {
        return favoriteAnimeDao.isFavorite(malId)
    }

    override fun isFavoriteFlow(malId: Int): Flow<Boolean> {
        return favoriteAnimeDao.isFavoriteFlow(malId)
    }

    override suspend fun addToFavorites(anime: Anime) {
        favoriteAnimeDao.insertFavorite(anime.toFavoriteEntity())
    }

    override suspend fun removeFromFavorites(malId: Int) {
        favoriteAnimeDao.deleteFavoriteById(malId)
    }

    override suspend fun toggleFavorite(anime: Anime): Boolean {
        val isFavorite = isFavorite(anime.malId)
        if (isFavorite) {
            removeFromFavorites(anime.malId)
            return false
        } else {
            addToFavorites(anime)
            return true
        }
    }

    override suspend fun clearAllFavorites() {
        favoriteAnimeDao.deleteAllFavorites()
    }

    override suspend fun getFavoriteCount(): Int {
        return favoriteAnimeDao.getFavoriteCount()
    }
}