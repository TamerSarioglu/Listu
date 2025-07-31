package com.tamersarioglu.listu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.usecase.GetTopAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAnimeViewModel @Inject constructor(
    private val getTopAnimeUseCase: GetTopAnimeUseCase
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
        page: Int = 1
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            getTopAnimeUseCase(
                type = type,
                filter = filter,
                rating = rating,
                page = page
            ).fold(
                onSuccess = { animeList ->
                    _uiState.value = _uiState.value.copy(
                        animeList = animeList,
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

    fun retry() {
        loadTopAnime()
    }
}

data class TopAnimeUiState(
    val animeList: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)