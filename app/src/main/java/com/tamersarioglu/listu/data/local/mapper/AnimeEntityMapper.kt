package com.tamersarioglu.listu.data.local.mapper

import com.tamersarioglu.listu.data.local.entity.AnimeEntity
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.model.topanimemodel.Image
import com.tamersarioglu.listu.domain.model.topanimemodel.ImageSet

fun Anime.toEntity(cacheType: String, page: Int = 1): AnimeEntity = AnimeEntity(
    malId = malId,
    url = url,
    imageUrl = images.jpg.imageUrl,
    smallImageUrl = images.jpg.smallImageUrl,
    largeImageUrl = images.jpg.largeImageUrl,
    title = title,
    titleEnglish = titleEnglish,
    type = type,
    episodes = episodes,
    status = status,
    score = score,
    rank = rank,
    popularity = popularity,
    members = members,
    favorites = favorites,
    cacheType = cacheType,
    page = page
)

fun AnimeEntity.toDomain(): Anime = Anime(
    malId = malId,
    url = url,
    images = ImageSet(
        jpg = Image(
            imageUrl = imageUrl,
            smallImageUrl = smallImageUrl,
            largeImageUrl = largeImageUrl
        ),
        webp = Image(
            imageUrl = imageUrl,
            smallImageUrl = smallImageUrl,
            largeImageUrl = largeImageUrl
        )
    ),
    title = title,
    titleEnglish = titleEnglish,
    type = type,
    episodes = episodes,
    status = status,
    score = score,
    rank = rank,
    popularity = popularity,
    members = members,
    favorites = favorites
)

fun List<Anime>.toEntityList(cacheType: String, page: Int = 1): List<AnimeEntity> =
    map { it.toEntity(cacheType, page) }

fun List<AnimeEntity>.toDomainList(): List<Anime> =
    map { it.toDomain() }