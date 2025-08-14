package com.tamersarioglu.listu.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tamersarioglu.listu.core.network.ConnectivityObserver
import com.tamersarioglu.listu.presentation.components.AnimeCard
import com.tamersarioglu.listu.presentation.components.ConnectionStatusBanner
import com.tamersarioglu.listu.presentation.components.SkeletonGrid
import com.tamersarioglu.listu.presentation.viewmodel.TopAnimeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAnimeScreen(
    onAnimeClick: (Int) -> Unit = {},
    viewModel: TopAnimeViewModel = hiltViewModel(),
    connectivityObserver: ConnectivityObserver
) {
    val uiState by viewModel.uiState.collectAsState()
    val connectionStatus by connectivityObserver.observe()
        .collectAsState(initial = ConnectivityObserver.Status.Available)
    val isConnected = connectionStatus == ConnectivityObserver.Status.Available

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Top Anime",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ConnectionStatusBanner(
                isConnected = isConnected,
                modifier = Modifier.fillMaxWidth()
            )

            val pullToRefreshState = rememberPullToRefreshState()

            PullToRefreshBox(
                isRefreshing = uiState.isLoading && uiState.animeList.isNotEmpty(),
                onRefresh = { viewModel.loadTopAnime() },
                state = pullToRefreshState,
                modifier = Modifier.weight(1f)
            ) {
                val gradientBackground = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradientBackground)
                ) {
                    val listState = rememberLazyListState()

                    when {
                        uiState.isLoading && uiState.animeList.isEmpty() -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(6) {
                                    SkeletonGrid(itemCount = 1)
                                }
                            }
                        }

                        uiState.error != null -> {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = uiState.error ?: "Unknown error",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Button(
                                    onClick = { viewModel.retry() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Text("Retry")
                                }
                            }
                        }

                        else -> {
                            if (uiState.animeList.isEmpty()) {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No results",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = "Pull to refresh",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    state = listState
                                ) {
                                    items(uiState.animeList) { anime ->
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
                                LaunchedEffect(
                                    listState,
                                    uiState.animeList.size,
                                    uiState.hasNextPage
                                ) {
                                    snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                                        .map { it }
                                        .filterNotNull()
                                        .distinctUntilChanged()
                                        .collectLatest { lastVisibleIndex ->
                                            val total = uiState.animeList.size
                                            if (uiState.hasNextPage && !uiState.isAppending && lastVisibleIndex >= total - 3) {
                                                viewModel.loadTopAnime(
                                                    page = uiState.currentPage + 1,
                                                    append = true
                                                )
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}