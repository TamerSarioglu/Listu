package com.tamersarioglu.listu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.listu.domain.model.SearchFilters
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.usecase.IsFavoriteUseCase
import com.tamersarioglu.listu.domain.usecase.SearchAnimeUseCase
import com.tamersarioglu.listu.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAnimeViewModel @Inject constructor(
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchAnimeUiState())
    val uiState: StateFlow<SearchAnimeUiState> = _uiState.asStateFlow()

    private var currentFilters = SearchFilters()

    fun updateQuery(query: String) {
        currentFilters = currentFilters.copy(query = query)
        _uiState.value = _uiState.value.copy(query = query)

        if (query.length >= 3) {
            search(page = 1, append = false)
        } else if (query.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                results = emptyList(),
                currentPage = 1,
                hasNextPage = false,
                error = null
            )
        }
    }

    fun search(page: Int, append: Boolean) {
        viewModelScope.launch {
            try {
                if (!append) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        error = null,
                        results = if (append) _uiState.value.results else emptyList()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isAppending = true)
                }

                searchAnimeUseCase(
                    query = currentFilters.query,
                    type = currentFilters.type?.apiValue,
                    status = currentFilters.status?.apiValue,
                    rating = currentFilters.rating?.apiValue,
                    genres = currentFilters.genre?.malId?.toString(),
                    producers = currentFilters.studio?.malId?.toString(),
                    startDate = currentFilters.year?.let { "${it}-01-01" },
                    minScore = currentFilters.minScore,
                    maxScore = currentFilters.maxScore,
                    orderBy = currentFilters.sortBy.apiValue,
                    sort = currentFilters.sortOrder.apiValue,
                    page = page,
                    limit = 25,
                    sfw = true
                ).fold(
                    onSuccess = { result ->
                        val newResults = if (append) {
                            _uiState.value.results + result.items
                        } else {
                            result.items
                        }
                        _uiState.value = _uiState.value.copy(
                            results = newResults,
                            currentPage = result.currentPage,
                            hasNextPage = result.hasNextPage,
                            isLoading = false,
                            isAppending = false,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isAppending = false,
                            error = exception.message
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAppending = false,
                    error = "Search failed: ${e.message}"
                )
            }
        }
    }

    fun searchWithFilters(filters: SearchFilters) {
        currentFilters = filters
        _uiState.value = _uiState.value.copy(query = filters.query)

        if (filters.query.isNotEmpty()) {
            search(page = 1, append = false)
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
}

data class SearchAnimeUiState(
    val query: String = "",
    val results: List<Anime> = emptyList(),
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val isLoading: Boolean = false,
    val isAppending: Boolean = false,
    val error: String? = null
)