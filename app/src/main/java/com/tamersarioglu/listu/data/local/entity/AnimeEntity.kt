package com.tamersarioglu.listu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_anime")
data class AnimeEntity(
    @PrimaryKey
    val malId: Int,
    val url: String,
    val imageUrl: String,
    val smallImageUrl: String?,
    val largeImageUrl: String?,
    val title: String,
    val titleEnglish: String?,
    val type: String,
    val episodes: Int?,
    val status: String,
    val score: Double?,
    val rank: Int?,
    val popularity: Int?,
    val members: Int?,
    val favorites: Int?,
    val cacheType: String, // "top", "search", etc.
    val cacheTimestamp: Long = System.currentTimeMillis(),
    val page: Int = 1
)