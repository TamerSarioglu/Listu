package com.tamersarioglu.listu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_anime")
data class FavoriteAnimeEntity(
    @PrimaryKey
    val malId: Int,
    val title: String,
    val titleEnglish: String?,
    val imageUrl: String,
    val score: Double?,
    val rank: Int?,
    val type: String,
    val status: String,
    val episodes: Int?,
    val dateAdded: Long = System.currentTimeMillis()
)