package com.tamersarioglu.listu.data.remote.dto.topanimedto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDto(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String,
    @SerialName("images") val images: ImageSetDto,
    @SerialName("title") val title: String,
    @SerialName("title_english") val titleEnglish: String? = null,
    @SerialName("type") val type: String,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("status") val status: String,
    @SerialName("score") val score: Double? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("popularity") val popularity: Int? = null,
    @SerialName("members") val members: Int? = null,
    @SerialName("favorites") val favorites: Int? = null
)

@Serializable
data class ImageSetDto(
    @SerialName("jpg") val jpg: ImageDto,
    @SerialName("webp") val webp: ImageDto
)

@Serializable
data class ImageDto(
    @SerialName("image_url") val imageUrl: String,
    @SerialName("small_image_url") val smallImageUrl: String? = null,
    @SerialName("large_image_url") val largeImageUrl: String? = null
)