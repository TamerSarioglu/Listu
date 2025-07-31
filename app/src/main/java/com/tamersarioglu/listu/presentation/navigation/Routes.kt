package com.tamersarioglu.listu.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object TopAnimeRoute

@Serializable
data class AnimeDetailRoute(val malId: Int)