package com.tamersarioglu.listu.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkOrange80,
    secondary = OrangeAccent,
    tertiary = VibrantOrange,
    background = DarkGrey90,
    surface = DarkGrey80,
    surfaceVariant = DarkGrey70,
    onPrimary = DarkGrey90,
    onSecondary = DarkGrey90,
    onTertiary = DarkGrey90,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = DarkGrey40,
    outlineVariant = DarkGrey60
)

private val LightColorScheme = lightColorScheme(
    primary = DarkOrange60,
    secondary = OrangeAccent,
    tertiary = VibrantOrange,
    background = DarkGrey80,
    surface = DarkGrey70,
    surfaceVariant = DarkGrey60,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = DarkGrey40,
    outlineVariant = DarkGrey60
)

@Composable
fun ListuTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}