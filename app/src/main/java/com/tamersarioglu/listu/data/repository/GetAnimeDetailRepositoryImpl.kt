package com.tamersarioglu.listu.data.repository

import com.tamersarioglu.listu.data.remote.api.animedetailservice.AnimeDetailService
import com.tamersarioglu.listu.data.remote.mapper.animedetailmapper.toDomain
import com.tamersarioglu.listu.domain.model.animedetailmodel.AnimeDetail
import com.tamersarioglu.listu.domain.repository.GetAnimeDetailRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAnimeDetailRepositoryImpl @Inject constructor(
    private val animeDetailService: AnimeDetailService
) : GetAnimeDetailRepository {
    
    override suspend fun getAnimeDetail(malId: Int): Result<AnimeDetail> {
        return try {
            val response = animeDetailService.getAnimeDetail(malId)
            val animeDetail = response.data.toDomain()
            Result.success(animeDetail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}