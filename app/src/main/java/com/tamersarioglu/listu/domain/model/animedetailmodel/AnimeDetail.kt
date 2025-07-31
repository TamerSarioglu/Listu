package com.tamersarioglu.listu.domain.model.animedetailmodel

import com.tamersarioglu.listu.domain.model.topanimemodel.ImageSet

data class AnimeDetail(
    val malId: Int,
    val url: String,
    val images: ImageSet,
    val trailer: Trailer? = null,
    val title: String,
    val titleEnglish: String? = null,
    val titleJapanese: String? = null,
    val titleSynonyms: List<String> = emptyList(),
    val type: String,
    val source: String? = null,
    val episodes: Int? = null,
    val status: String,
    val airing: Boolean = false,
    val aired: Aired? = null,
    val duration: String? = null,
    val rating: String? = null,
    val score: Double? = null,
    val scoredBy: Int? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    val members: Int? = null,
    val favorites: Int? = null,
    val synopsis: String? = null,
    val background: String? = null,
    val season: String? = null,
    val year: Int? = null,
    val genres: List<Genre> = emptyList(),
    val studios: List<Studio> = emptyList(),
    val producers: List<Producer> = emptyList()
)

data class Trailer(
    val youtubeId: String? = null,
    val url: String? = null,
    val embedUrl: String? = null
)

data class Aired(
    val from: String? = null,
    val to: String? = null,
    val string: String? = null
)

data class Genre(
    val malId: Int,
    val type: String,
    val name: String,
    val url: String
)

data class Studio(
    val malId: Int,
    val type: String,
    val name: String,
    val url: String
)

data class Producer(
    val malId: Int,
    val type: String,
    val name: String,
    val url: String
)