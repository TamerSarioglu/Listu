package com.tamersarioglu.listu.data.remote.mapper.animedetailmapper

import com.tamersarioglu.listu.data.remote.dto.animedetaildto.*
import com.tamersarioglu.listu.data.remote.mapper.topanimemapper.toDomain
import com.tamersarioglu.listu.domain.model.animedetailmodel.*

fun AnimeDetailDto.toDomain(): AnimeDetail = AnimeDetail(
    malId = malId,
    url = url,
    images = images.toDomain(),
    trailer = trailer?.toDomain(),
    title = title,
    titleEnglish = titleEnglish,
    titleJapanese = titleJapanese,
    titleSynonyms = titleSynonyms,
    type = type,
    source = source,
    episodes = episodes,
    status = status,
    airing = airing,
    aired = aired?.toDomain(),
    duration = duration,
    rating = rating,
    score = score,
    scoredBy = scoredBy,
    rank = rank,
    popularity = popularity,
    members = members,
    favorites = favorites,
    synopsis = synopsis,
    background = background,
    season = season,
    year = year,
    genres = genres.map { it.toDomain() },
    studios = studios.map { it.toDomain() },
    producers = producers.map { it.toDomain() }
)

fun TrailerDto.toDomain(): Trailer = Trailer(
    youtubeId = youtubeId,
    url = url,
    embedUrl = embedUrl
)

fun AiredDto.toDomain(): Aired = Aired(
    from = from,
    to = to,
    string = string
)

fun GenreDto.toDomain(): Genre = Genre(
    malId = malId,
    type = type,
    name = name,
    url = url
)

fun StudioDto.toDomain(): Studio = Studio(
    malId = malId,
    type = type,
    name = name,
    url = url
)

fun ProducerDto.toDomain(): Producer = Producer(
    malId = malId,
    type = type,
    name = name,
    url = url
)