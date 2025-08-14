package com.tamersarioglu.listu.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun SkeletonLoader(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation, y = translateAnimation)
    )

    Box(
        modifier = modifier
            .background(brush)
            .clip(RoundedCornerShape(4.dp))
    )
}

@Composable
fun AnimeCardSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image placeholder
            SkeletonLoader(
                modifier = Modifier.size(80.dp, 120.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title placeholder
                SkeletonLoader(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(20.dp)
                )

                // Subtitle placeholder
                SkeletonLoader(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(16.dp)
                )

                // Chips row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SkeletonLoader(
                        modifier = Modifier
                            .width(60.dp)
                            .height(24.dp)
                    )
                    SkeletonLoader(
                        modifier = Modifier
                            .width(80.dp)
                            .height(24.dp)
                    )
                }

                // Score row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SkeletonLoader(
                        modifier = Modifier
                            .width(16.dp)
                            .height(16.dp)
                    )
                    SkeletonLoader(
                        modifier = Modifier
                            .width(40.dp)
                            .height(16.dp)
                    )
                    SkeletonLoader(
                        modifier = Modifier
                            .width(60.dp)
                            .height(16.dp)
                    )
                }

                // Status chip
                SkeletonLoader(
                    modifier = Modifier
                        .width(100.dp)
                        .height(24.dp)
                )
            }
        }
    }
}

@Composable
fun SkeletonGrid(
    modifier: Modifier = Modifier,
    itemCount: Int = 6
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(itemCount) {
            AnimeCardSkeleton()
        }
    }
}