package com.tamersarioglu.listu.data.remote.mapper.topanimemapper

import com.tamersarioglu.listu.data.remote.dto.topanimedto.AnimeDto
import com.tamersarioglu.listu.data.remote.dto.topanimedto.ImageDto
import com.tamersarioglu.listu.data.remote.dto.topanimedto.ImageSetDto
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.model.topanimemodel.Image
import com.tamersarioglu.listu.domain.model.topanimemodel.ImageSet

fun AnimeDto.toDomain(): Anime = Anime(
    malId = malId,
    url = url,
    images = images.toDomain(),
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

fun ImageDto.toDomain(): Image = Image(
    imageUrl = imageUrl,
    smallImageUrl = smallImageUrl,
    largeImageUrl = largeImageUrl
)

fun ImageSetDto.toDomain(): ImageSet = ImageSet(
    jpg = jpg.toDomain(),
    webp = webp.toDomain()
)