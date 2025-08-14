package com.tamersarioglu.listu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.listu.domain.model.CacheSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    // TODO: Add PreferencesRepository when implemented
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun updateNsfwContent(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(showNsfwContent = enabled)
        // TODO: Save to preferences
    }

    fun updateAutoRefresh(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoRefresh = enabled)
        // TODO: Save to preferences
    }

    fun updateNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
        // TODO: Save to preferences
    }

    fun updateCacheSize(size: CacheSize) {
        _uiState.value = _uiState.value.copy(cacheSize = size)
        // TODO: Save to preferences
    }

    fun updateReduceMotion(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(reduceMotion = enabled)
        // TODO: Save to preferences
    }

    fun clearCache() {
        viewModelScope.launch {
            try {
                // TODO: Implement cache clearing
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Cache cleared successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to clear cache: ${e.message}"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

data class SettingsUiState(
    val showNsfwContent: Boolean = false,
    val autoRefresh: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val cacheSize: CacheSize = CacheSize.MEDIUM,
    val reduceMotion: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)