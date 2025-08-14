package com.tamersarioglu.listu.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tamersarioglu.listu.presentation.components.AnimeCard
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = { viewModel.updateQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Search anime...") },
                trailingIcon = {
                    IconButton(onClick = { viewModel.search(page = 1, append = false) }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )

            if (uiState.isLoading && uiState.results.isEmpty()) {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else if (uiState.error != null && uiState.results.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(uiState.error ?: "Unknown error")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        viewModel.search(
                            page = 1,
                            append = false
                        )
                    }) { Text("Retry") }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.results) { anime ->
                        AnimeCard(anime = anime, onAnimeClick = onAnimeClick)
                    }
                    if (uiState.isAppending) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) { CircularProgressIndicator() }
                        }
                    }
                }

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
}