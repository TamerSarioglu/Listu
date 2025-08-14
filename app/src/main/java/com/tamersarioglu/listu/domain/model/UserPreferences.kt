package com.tamersarioglu.listu.domain.model

data class UserPreferences(
    val isDarkTheme: Boolean = false,
    val isSystemTheme: Boolean = true,
    val language: String = "en",
    val showNsfwContent: Boolean = false,
    val enableNotifications: Boolean = true,
    val autoRefresh: Boolean = true,
    val cacheSize: CacheSize = CacheSize.MEDIUM
)

enum class CacheSize(val displayName: String, val sizeInMB: Int) {
    SMALL("Small (50MB)", 50),
    MEDIUM("Medium (100MB)", 100),
    LARGE("Large (200MB)", 200)
}

enum class AppTheme(val displayName: String) {
    SYSTEM("Follow System"),
    LIGHT("Light"),
    DARK("Dark")
}