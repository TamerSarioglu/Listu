package com.tamersarioglu.listu.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.tamersarioglu.listu.domain.model.animedetailmodel.AnimeDetail
import com.tamersarioglu.listu.presentation.viewmodel.AnimeDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreen(
    malId: Int,
    onBackClick: () -> Unit,
    viewModel: AnimeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite(malId).collectAsState(initial = false)

    LaunchedEffect(malId) {
        viewModel.loadAnimeDetail(malId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (uiState.animeDetail != null) {
                FloatingActionButton(
                    onClick = { viewModel.toggleFavorite() },
                    containerColor = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    contentColor = if (isFavorite) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
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
                            onClick = { viewModel.retry(malId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }

                uiState.animeDetail != null -> {
                    AnimeDetailContent(
                        animeDetail = uiState.animeDetail!!,
                        onBackClick = onBackClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeDetailContent(
    animeDetail: AnimeDetail,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val trailerUrl: String? = animeDetail.trailer?.url ?: animeDetail.trailer?.embedUrl
    val youtubeId: String? = animeDetail.trailer?.youtubeId
    val youtubeUrlFromId: String? = youtubeId?.let { "https://www.youtube.com/watch?v=$it" }
    val youtubeUrlFromFields: String? = listOf(trailerUrl, animeDetail.trailer?.embedUrl)
        .firstOrNull { it?.contains("youtu", ignoreCase = true) == true }
    val youtubeOpenUrl: String = youtubeUrlFromId
        ?: youtubeUrlFromFields
        ?: ("https://www.youtube.com/results?search_query=" +
                (animeDetail.title.replace(' ', '+') + "+trailer"))

    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header image with overlay content
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                AsyncImage(
                    model = animeDetail.images.jpg.largeImageUrl ?: animeDetail.images.jpg.imageUrl,
                    contentDescription = animeDetail.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradient scrim for readability
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.25f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )

                // Top actions (back)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    FilledTonalIconButton(
                        onClick = onBackClick,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

                // Bottom title and meta
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = animeDetail.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    animeDetail.titleEnglish?.let { englishTitle ->
                        Text(
                            text = englishTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Chips row under header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(animeDetail.type) }
                )
                animeDetail.episodes?.let { episodes ->
                    AssistChip(onClick = { }, label = { Text("$episodes eps") })
                }
                AssistChip(
                    onClick = { },
                    label = { Text(animeDetail.status) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (animeDetail.status.lowercase()) {
                            "currently airing" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                            "finished airing" -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                )
            }
        }

        // Watch Trailer button - opens YouTube
        item {
            Button(
                onClick = { uriHandler.openUri(youtubeOpenUrl) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Watch Trailer on YouTube")
            }
        }

        // Statistics Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Statistics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        animeDetail.score?.let { score ->
                            StatCard(
                                icon = Icons.Default.Star,
                                value = score.toString(),
                                label = "Score",
                                iconTint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        animeDetail.rank?.let { rank ->
                            StatCard(
                                icon = Icons.Default.Star,
                                value = "#$rank",
                                label = "Rank",
                                iconTint = MaterialTheme.colorScheme.primary
                            )
                        }
                        animeDetail.members?.let { members ->
                            StatCard(
                                icon = Icons.Default.People,
                                value = formatNumber(members),
                                label = "Members",
                                iconTint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        animeDetail.favorites?.let { favorites ->
                            StatCard(
                                icon = Icons.Default.Favorite,
                                value = formatNumber(favorites),
                                label = "Favorites",
                                iconTint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }

        // Genres Section
        if (animeDetail.genres.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Genres",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(animeDetail.genres) { genre ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(genre.name) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Synopsis Section
        animeDetail.synopsis?.let { synopsis ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Synopsis",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            text = synopsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Information Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Information",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    InfoRow("Type", animeDetail.type)
                    animeDetail.episodes?.let { InfoRow("Episodes", it.toString()) }
                    InfoRow("Status", animeDetail.status)
                    animeDetail.aired?.string?.let { InfoRow("Aired", it) }
                    animeDetail.duration?.let { InfoRow("Duration", it) }
                    animeDetail.rating?.let { InfoRow("Rating", it) }
                    animeDetail.source?.let { InfoRow("Source", it) }
                    animeDetail.popularity?.let { InfoRow("Popularity", "#$it") }
                    if (animeDetail.studios.isNotEmpty()) {
                        InfoRow("Studio", animeDetail.studios.first().name)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { uriHandler.openUri(animeDetail.url) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("View on MyAnimeList")
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    iconTint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> "${number / 1_000_000}M"
        number >= 1_000 -> "${number / 1_000}K"
        else -> number.toString()
    }
}