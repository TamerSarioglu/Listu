package com.tamersarioglu.listu.domain.model.topanimemodel


import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    val malId: Int,
    val url: String,
    val images: ImageSet,
    val title: String,
    val titleEnglish: String? = null,
    val type: String,
    val episodes: Int? = null,
    val status: String,
    val score: Double? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    val members: Int? = null,
    val favorites: Int? = null
)

@Serializable
data class ImageSet(
    val jpg: Image,
    val webp: Image
)

@Serializable
data class Image(
    val imageUrl: String,
    val smallImageUrl: String? = null,
    val largeImageUrl: String? = null
)
