package com.tamersarioglu.listu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.listu.data.local.mapper.toAnime
import com.tamersarioglu.listu.domain.model.animedetailmodel.AnimeDetail
import com.tamersarioglu.listu.domain.usecase.GetAnimeDetailUseCase
import com.tamersarioglu.listu.domain.usecase.IsFavoriteUseCase
import com.tamersarioglu.listu.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val getAnimeDetailUseCase: GetAnimeDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimeDetailUiState())
    val uiState: StateFlow<AnimeDetailUiState> = _uiState.asStateFlow()

    fun loadAnimeDetail(malId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            getAnimeDetailUseCase(malId).fold(
                onSuccess = { animeDetail ->
                    _uiState.value = _uiState.value.copy(
                        animeDetail = animeDetail,
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    fun toggleFavorite() {
        val animeDetail = _uiState.value.animeDetail ?: return
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(animeDetail.toAnime())
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

    fun retry(malId: Int) {
        loadAnimeDetail(malId)
    }
}

data class AnimeDetailUiState(
    val animeDetail: AnimeDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)