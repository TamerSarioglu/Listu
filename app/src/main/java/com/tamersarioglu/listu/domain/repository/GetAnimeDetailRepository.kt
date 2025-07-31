package com.tamersarioglu.listu.domain.repository

import com.tamersarioglu.listu.domain.model.animedetailmodel.AnimeDetail

interface GetAnimeDetailRepository {
    suspend fun getAnimeDetail(malId: Int): Result<AnimeDetail>
}