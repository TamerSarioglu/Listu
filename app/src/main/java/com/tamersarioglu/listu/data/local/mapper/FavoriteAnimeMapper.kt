package com.tamersarioglu.listu.data.local.mapper

import com.tamersarioglu.listu.data.local.entity.FavoriteAnimeEntity
import com.tamersarioglu.listu.domain.model.animedetailmodel.AnimeDetail
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.model.topanimemodel.Image
import com.tamersarioglu.listu.domain.model.topanimemodel.ImageSet

fun Anime.toFavoriteEntity(): FavoriteAnimeEntity = FavoriteAnimeEntity(
    malId = malId,
    title = title,
    titleEnglish = titleEnglish,
    imageUrl = images.jpg.imageUrl,
    score = score,
    rank = rank,
    type = type,
    status = status,
    episodes = episodes
)

fun AnimeDetail.toAnime(): Anime = Anime(
    malId = malId,
    url = url,
    images = images,
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

fun FavoriteAnimeEntity.toDomain(): Anime = Anime(
    malId = malId,
    url = "", // Not stored in favorites
    images = ImageSet(
        jpg = Image(
            imageUrl = imageUrl
        ),
        webp = Image(
            imageUrl = imageUrl
        )
    ),
    title = title,
    titleEnglish = titleEnglish,
    type = type,
    episodes = episodes,
    status = status,
    score = score,
    rank = rank,
    popularity = null, // Not stored in favorites
    members = null, // Not stored in favorites
    favorites = null // Not stored in favorites
)