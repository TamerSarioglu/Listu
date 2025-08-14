package com.tamersarioglu.listu.domain.repository

import com.tamersarioglu.listu.domain.model.AppTheme
import com.tamersarioglu.listu.domain.model.CacheSize
import com.tamersarioglu.listu.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getUserPreferences(): Flow<UserPreferences>
    suspend fun updateTheme(theme: AppTheme)
    suspend fun updateLanguage(language: String)
    suspend fun updateNsfwContent(enabled: Boolean)
    suspend fun updateNotifications(enabled: Boolean)
    suspend fun updateAutoRefresh(enabled: Boolean)
    suspend fun updateCacheSize(cacheSize: CacheSize)
    suspend fun clearAllData()
    suspend fun resetToDefaults()
}