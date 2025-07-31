package com.tamersarioglu.listu.data.remote.dto.topanimedto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopAnimeResponseDto(
    @SerialName("pagination") val pagination: PaginationDto,
    @SerialName("data") val data: List<AnimeDto>
)

@Serializable
data class PaginationDto(
    @SerialName("last_visible_page") val lastVisiblePage: Int,
    @SerialName("has_next_page") val hasNextPage: Boolean,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("items") val items: PaginationItemsDto
)

@Serializable
data class PaginationItemsDto(
    @SerialName("count") val count: Int,
    @SerialName("total") val total: Int,
    @SerialName("per_page") val perPage: Int
)