package com.tamersarioglu.listu.data.remote.dto.animedetaildto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailResponseDto(
    @SerialName("data") val data: AnimeDetailDto
)