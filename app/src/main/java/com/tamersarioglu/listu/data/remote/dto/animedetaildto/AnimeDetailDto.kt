package com.tamersarioglu.listu.data.remote.dto.animedetaildto

import com.tamersarioglu.listu.data.remote.dto.topanimedto.ImageSetDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailDto(
    @SerialName("mal_id") val malId: Int,
    @SerialName("url") val url: String,
    @SerialName("images") val images: ImageSetDto,
    @SerialName("trailer") val trailer: TrailerDto? = null,
    @SerialName("title") val title: String,
    @SerialName("title_english") val titleEnglish: String? = null,
    @SerialName("title_japanese") val titleJapanese: String? = null,
    @SerialName("title_synonyms") val titleSynonyms: List<String> = emptyList(),
    @SerialName("type") val type: String,
    @SerialName("source") val source: String? = null,
    @SerialName("episodes") val episodes: Int? = null,
    @SerialName("status") val status: String,
    @SerialName("airing") val airing: Boolean = false,
    @SerialName("aired") val aired: AiredDto? = null,
    @SerialName("duration") val duration: String? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("score") val score: Double? = null,
    @SerialName("scored_by") val scoredBy: Int? = null,
    @SerialName("rank") val rank: Int? = null,
    @SerialName("popularity") val popularity: Int? = null,
    @SerialName("members") val members: Int? = null,
    @SerialName("favorites") val favorites: Int? = null,
    @SerialName("synopsis") val synopsis: String? = null,
    @SerialName("background") val background: String? = null,
    @SerialName("season") val season: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("genres") val genres: List<GenreDto> = emptyList(),
    @SerialName("studios") val studios: List<StudioDto> = emptyList(),
    @SerialName("producers") val producers: List<ProducerDto> = emptyList()
)

@Serializable
data class TrailerDto(
    @SerialName("youtube_id") val youtubeId: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("embed_url") val embedUrl: String? = null
)

@Serializable
data class AiredDto(
    @SerialName("from") val from: String? = null,
    @SerialName("to") val to: String? = null,
    @SerialName("string") val string: String? = null
)

@Serializable
data class GenreDto(
    @SerialName("mal_id") val malId: Int,
    @SerialName("type") val type: String,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class StudioDto(
    @SerialName("mal_id") val malId: Int,
    @SerialName("type") val type: String,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class ProducerDto(
    @SerialName("mal_id") val malId: Int,
    @SerialName("type") val type: String,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)