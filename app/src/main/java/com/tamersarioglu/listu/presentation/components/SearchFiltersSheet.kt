package com.tamersarioglu.listu.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamersarioglu.listu.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFiltersSheet(
    filters: SearchFilters,
    onFiltersChanged: (SearchFilters) -> Unit,
    onDismiss: () -> Unit,
    onApplyFilters: () -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        dragHandle = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                BottomSheetDefaults.DragHandle()
                Text(
                    text = "Filter & Sort",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Type Filter
            item {
                FilterSection(
                    title = "Type",
                    icon = Icons.Default.Category
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = filters.type == null,
                                onClick = { onFiltersChanged(filters.copy(type = null)) },
                                label = { Text("All") }
                            )
                        }
                        items(AnimeType.entries) { type ->
                            FilterChip(
                                selected = filters.type == type,
                                onClick = { onFiltersChanged(filters.copy(type = type)) },
                                label = { Text(type.displayName) }
                            )
                        }
                    }
                }
            }

            // Status Filter
            item {
                FilterSection(
                    title = "Status",
                    icon = Icons.Default.PlayArrow
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = filters.status == null,
                                onClick = { onFiltersChanged(filters.copy(status = null)) },
                                label = { Text("All") }
                            )
                        }
                        items(AnimeStatus.entries) { status ->
                            FilterChip(
                                selected = filters.status == status,
                                onClick = { onFiltersChanged(filters.copy(status = status)) },
                                label = { Text(status.displayName) }
                            )
                        }
                    }
                }
            }

            // Genre Filter
            item {
                FilterSection(
                    title = "Genre",
                    icon = Icons.Default.LocalMovies
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = filters.genre == null,
                                onClick = { onFiltersChanged(filters.copy(genre = null)) },
                                label = { Text("All") }
                            )
                        }
                        items(Genre.POPULAR_GENRES) { genre ->
                            FilterChip(
                                selected = filters.genre?.malId == genre.malId,
                                onClick = { onFiltersChanged(filters.copy(genre = genre)) },
                                label = { Text(genre.name) }
                            )
                        }
                    }
                }
            }

            // Year Filter
            item {
                FilterSection(
                    title = "Year",
                    icon = Icons.Default.DateRange
                ) {
                    val currentYear = 2024
                    val years = (currentYear downTo 1960).toList()

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = filters.year == null,
                                onClick = { onFiltersChanged(filters.copy(year = null)) },
                                label = { Text("All") }
                            )
                        }
                        items(years.take(20)) { year -> // Show last 20 years
                            FilterChip(
                                selected = filters.year == year,
                                onClick = { onFiltersChanged(filters.copy(year = year)) },
                                label = { Text(year.toString()) }
                            )
                        }
                    }
                }
            }

            // Score Range
            item {
                FilterSection(
                    title = "Score Range",
                    icon = Icons.Default.Star
                ) {
                    ScoreRangeSlider(
                        minScore = filters.minScore,
                        maxScore = filters.maxScore,
                        onRangeChanged = { min, max ->
                            onFiltersChanged(
                                filters.copy(
                                    minScore = min,
                                    maxScore = max
                                )
                            )
                        }
                    )
                }
            }

            // Sort Options
            item {
                FilterSection(
                    title = "Sort By",
                    icon = Icons.AutoMirrored.Filled.Sort
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(SortOption.entries.take(6)) { sortOption ->
                                FilterChip(
                                    selected = filters.sortBy == sortOption,
                                    onClick = { onFiltersChanged(filters.copy(sortBy = sortOption)) },
                                    label = { Text(sortOption.displayName) }
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Order:")
                            SortOrder.entries.forEach { order ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.selectable(
                                        selected = filters.sortOrder == order,
                                        onClick = { onFiltersChanged(filters.copy(sortOrder = order)) },
                                        role = Role.RadioButton
                                    )
                                ) {
                                    RadioButton(
                                        selected = filters.sortOrder == order,
                                        onClick = null
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(order.displayName)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                            }
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onResetFilters,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reset")
                    }

                    Button(
                        onClick = {
                            onApplyFilters()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Done, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Apply")
                    }
                }
            }

            // Bottom padding for safe area
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScoreRangeSlider(
    minScore: Double?,
    maxScore: Double?,
    onRangeChanged: (Double?, Double?) -> Unit
) {
    var sliderPosition by remember {
        mutableStateOf(
            (minScore ?: 0.0f).toFloat()..(maxScore ?: 10.0f).toFloat()
        )
    }

    Column {
        Text(
            text = "Score: ${String.format("%.1f", sliderPosition.start)} - ${
                String.format(
                    "%.1f",
                    sliderPosition.endInclusive
                )
            }",
            style = MaterialTheme.typography.bodyMedium
        )

        RangeSlider(
            value = sliderPosition,
            onValueChange = { range ->
                sliderPosition = range
                val min = if (range.start <= 0.1f) null else range.start.toDouble()
                val max = if (range.endInclusive >= 9.9f) null else range.endInclusive.toDouble()
                onRangeChanged(min, max)
            },
            valueRange = 0f..10f,
            steps = 19, // 0.5 increments
            modifier = Modifier.fillMaxWidth()
        )
    }
}