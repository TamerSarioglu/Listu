package com.tamersarioglu.listu.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tamersarioglu.listu.domain.model.SearchFilters
import com.tamersarioglu.listu.presentation.components.AnimeCard
import com.tamersarioglu.listu.presentation.components.SearchFiltersSheet
import com.tamersarioglu.listu.presentation.components.SkeletonGrid
import com.tamersarioglu.listu.presentation.viewmodel.SearchAnimeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onAnimeClick: (Int) -> Unit = {},
    viewModel: SearchAnimeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var showFilters by remember { mutableStateOf(false) }
    var searchFilters by remember { mutableStateOf(SearchFilters()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Search",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showFilters = true }
                    ) {
                        Badge(
                            containerColor = if (hasActiveFilters(searchFilters))
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filters",
                                tint = if (hasActiveFilters(searchFilters))
                                    MaterialTheme.colorScheme.onError
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchFilters.query,
                onValueChange = { query ->
                    searchFilters = searchFilters.copy(query = query)
                    viewModel.updateQuery(query)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Search anime...") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (hasActiveFilters(searchFilters)) {
                        IconButton(onClick = { showFilters = true }) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error
                            ) {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = "Active filters",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onError
                                )
                            }
                        }
                    }
                },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Search
                ),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onSearch = {
                        viewModel.searchWithFilters(searchFilters)
                    }
                )
            )

            // Active Filters Display
            if (hasActiveFilters(searchFilters)) {
                ActiveFiltersDisplay(
                    filters = searchFilters,
                    onRemoveFilter = { updatedFilters ->
                        searchFilters = updatedFilters
                        if (searchFilters.query.isNotEmpty()) {
                            viewModel.searchWithFilters(searchFilters)
                        }
                    }
                )
            }

            // Search Results
            when {
                uiState.isLoading && uiState.results.isEmpty() -> {
                    SkeletonGrid(itemCount = 6)
                }

                uiState.error != null && uiState.results.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.searchWithFilters(searchFilters) }
                        ) {
                            Text("Retry")
                        }
                    }
                }

                searchFilters.query.isEmpty() -> {
                    SearchEmptyState()
                }

                uiState.results.isEmpty() && !uiState.isLoading -> {
                    NoResultsState(
                        query = searchFilters.query,
                        onClearFilters = {
                            searchFilters = SearchFilters(query = searchFilters.query)
                            viewModel.searchWithFilters(searchFilters)
                        }
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.results,
                            key = { it.malId }
                        ) { anime ->
                            val isFavorite by viewModel.isFavorite(anime.malId)
                                .collectAsState(initial = false)

                            AnimeCard(
                                anime = anime,
                                onAnimeClick = onAnimeClick,
                                isFavorite = isFavorite,
                                onFavoriteClick = { viewModel.toggleFavorite(it) }
                            )
                        }

                        if (uiState.isAppending) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }

                    // Pagination
                    LaunchedEffect(listState, uiState.results.size, uiState.hasNextPage) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                            .filterNotNull()
                            .distinctUntilChanged()
                            .collectLatest { lastIndex ->
                                val total = uiState.results.size
                                if (uiState.hasNextPage && !uiState.isAppending && lastIndex >= total - 3) {
                                    viewModel.search(page = uiState.currentPage + 1, append = true)
                                }
                            }
                    }
                }
            }
        }

        // Filters Bottom Sheet
        if (showFilters) {
            SearchFiltersSheet(
                filters = searchFilters,
                onFiltersChanged = { searchFilters = it },
                onDismiss = { showFilters = false },
                onApplyFilters = {
                    if (searchFilters.query.isNotEmpty()) {
                        viewModel.searchWithFilters(searchFilters)
                    }
                },
                onResetFilters = {
                    searchFilters = SearchFilters(query = searchFilters.query)
                    if (searchFilters.query.isNotEmpty()) {
                        viewModel.searchWithFilters(searchFilters)
                    }
                }
            )
        }
    }
}

@Composable
private fun ActiveFiltersDisplay(
    filters: SearchFilters,
    onRemoveFilter: (SearchFilters) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.type?.let { type ->
            item {
                InputChip(
                    selected = true,
                    onClick = { onRemoveFilter(filters.copy(type = null)) },
                    label = { Text("Type: ${type.displayName}") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }

        filters.status?.let { status ->
            item {
                InputChip(
                    selected = true,
                    onClick = { onRemoveFilter(filters.copy(status = null)) },
                    label = { Text("Status: ${status.displayName}") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }

        filters.genre?.let { genre ->
            item {
                InputChip(
                    selected = true,
                    onClick = { onRemoveFilter(filters.copy(genre = null)) },
                    label = { Text("Genre: ${genre.name}") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }

        filters.year?.let { year ->
            item {
                InputChip(
                    selected = true,
                    onClick = { onRemoveFilter(filters.copy(year = null)) },
                    label = { Text("Year: $year") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Search for Anime",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter a search term or use filters to discover anime",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NoResultsState(
    query: String,
    onClearFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Results Found",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try adjusting your search or filters for \"$query\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onClearFilters) {
            Text("Clear Filters")
        }
    }
}

private fun hasActiveFilters(filters: SearchFilters): Boolean {
    return filters.type != null ||
            filters.status != null ||
            filters.genre != null ||
            filters.year != null ||
            filters.minScore != null ||
            filters.maxScore != null ||
            filters.sortBy != SearchFilters().sortBy ||
            filters.sortOrder != SearchFilters().sortOrder
}