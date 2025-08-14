package com.tamersarioglu.listu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.model.topanimemodel.TopAnimePage
import com.tamersarioglu.listu.domain.usecase.GetTopAnimeUseCase
import com.tamersarioglu.listu.domain.usecase.IsFavoriteUseCase
import com.tamersarioglu.listu.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAnimeViewModel @Inject constructor(
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TopAnimeUiState())
    val uiState: StateFlow<TopAnimeUiState> = _uiState.asStateFlow()

    init {
        loadTopAnime()
    }

    fun loadTopAnime(
        type: String? = null,
        filter: String? = null,
        rating: String? = null,
        page: Int = 1,
        append: Boolean = false
    ) {
        viewModelScope.launch {
            if (append) {
                if (_uiState.value.isAppending || !_uiState.value.hasNextPage) return@launch
                _uiState.value = _uiState.value.copy(isAppending = true, error = null)
            } else {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }
            
            getTopAnimeUseCase(
                type = type,
                filter = filter,
                rating = rating,
                page = page
            ).fold(
                onSuccess = { pageResult: TopAnimePage ->
                    if (append) {
                        _uiState.value = _uiState.value.copy(
                            animeList = _uiState.value.animeList + pageResult.items,
                            isAppending = false,
                            error = null,
                            currentPage = pageResult.currentPage,
                            hasNextPage = pageResult.hasNextPage
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            animeList = pageResult.items,
                            isLoading = false,
                            error = null,
                            currentPage = pageResult.currentPage,
                            hasNextPage = pageResult.hasNextPage
                        )
                    }
                },
                onFailure = { exception ->
                    if (append) {
                        _uiState.value = _uiState.value.copy(
                            isAppending = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                }
            )
        }
    }

    fun toggleFavorite(anime: Anime) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(anime)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update favorites: ${e.message}"
                )
            }
        }
    }

    fun isFavorite(malId: Int): kotlinx.coroutines.flow.Flow<Boolean> {
        return isFavoriteUseCase.asFlow(malId)
    }

    fun retry() {
        loadTopAnime()
    }
}

data class TopAnimeUiState(
    val animeList: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val isAppending: Boolean = false,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = true,
    val error: String? = null
)