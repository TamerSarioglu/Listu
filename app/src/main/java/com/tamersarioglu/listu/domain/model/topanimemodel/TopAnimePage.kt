package com.tamersarioglu.listu.domain.model.topanimemodel

data class TopAnimePage(
    val currentPage: Int,
    val hasNextPage: Boolean,
    val items: List<Anime>
)