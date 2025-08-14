package com.tamersarioglu.listu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamersarioglu.listu.domain.model.topanimemodel.Anime
import com.tamersarioglu.listu.domain.model.topanimemodel.TopAnimePage
import com.tamersarioglu.listu.domain.usecase.SearchAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAnimeViewModel @Inject constructor(
    private val searchAnimeUseCase: SearchAnimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchAnimeUiState())
    val uiState: StateFlow<SearchAnimeUiState> = _uiState.asStateFlow()

    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun search(page: Int = 1, append: Boolean = false) {
        val currentQuery = _uiState.value.query
        viewModelScope.launch {
            if (append) {
                if (_uiState.value.isAppending || !_uiState.value.hasNextPage) return@launch
                _uiState.value = _uiState.value.copy(isAppending = true, error = null)
            } else {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }

            searchAnimeUseCase(
                query = currentQuery,
                page = page,
                limit = 25,
                sfw = true
            ).fold(
                onSuccess = { result: TopAnimePage ->
                    if (append) {
                        _uiState.value = _uiState.value.copy(
                            results = _uiState.value.results + result.items,
                            isAppending = false,
                            currentPage = result.currentPage,
                            hasNextPage = result.hasNextPage
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            results = result.items,
                            isLoading = false,
                            currentPage = result.currentPage,
                            hasNextPage = result.hasNextPage
                        )
                    }
                },
                onFailure = { e ->
                    if (append) {
                        _uiState.value = _uiState.value.copy(isAppending = false, error = e.message)
                    } else {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                    }
                }
            )
        }
    }
}

data class SearchAnimeUiState(
    val query: String = "",
    val results: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val isAppending: Boolean = false,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val error: String? = null
)