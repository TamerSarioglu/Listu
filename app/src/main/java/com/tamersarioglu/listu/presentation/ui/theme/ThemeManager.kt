package com.tamersarioglu.listu.presentation.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tamersarioglu.listu.domain.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

@Singleton
class ThemeManager @Inject constructor() {

    private val _currentTheme = MutableStateFlow(AppTheme.SYSTEM)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    private val _accentColor = MutableStateFlow(AccentColor.PURPLE)
    val accentColor: StateFlow<AccentColor> = _accentColor.asStateFlow()

    private val _useDynamicColor = MutableStateFlow(true)
    val useDynamicColor: StateFlow<Boolean> = _useDynamicColor.asStateFlow()

    private val _useHighContrast = MutableStateFlow(false)
    val useHighContrast: StateFlow<Boolean> = _useHighContrast.asStateFlow()

    fun setTheme(theme: AppTheme) {
        _currentTheme.value = theme
    }

    fun setAccentColor(color: AccentColor) {
        _accentColor.value = color
    }

    fun setUseDynamicColor(use: Boolean) {
        _useDynamicColor.value = use
    }

    fun setUseHighContrast(use: Boolean) {
        _useHighContrast.value = use
    }

    @Composable
    fun getColorScheme(
        context: Context = LocalContext.current
    ): ColorScheme {
        val theme by currentTheme.collectAsState()
        val accent by accentColor.collectAsState()
        val dynamicColor by useDynamicColor.collectAsState()
        val highContrast by useHighContrast.collectAsState()

        val isDarkTheme = when (theme) {
            AppTheme.SYSTEM -> isSystemInDarkTheme()
            AppTheme.LIGHT -> false
            AppTheme.DARK -> true
        }

        return when {
            dynamicColor && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S -> {
                if (isDarkTheme) {
                    dynamicDarkColorScheme(context).let { scheme ->
                        if (highContrast) scheme.toHighContrast() else scheme
                    }
                } else {
                    dynamicLightColorScheme(context).let { scheme ->
                        if (highContrast) scheme.toHighContrast() else scheme
                    }
                }
            }

            else -> {
                val baseScheme = if (isDarkTheme) {
                    createDarkColorScheme(accent)
                } else {
                    createLightColorScheme(accent)
                }
                if (highContrast) baseScheme.toHighContrast() else baseScheme
            }
        }
    }

    private fun createLightColorScheme(accent: AccentColor): ColorScheme {
        return lightColorScheme(
            primary = accent.primary,
            onPrimary = accent.onPrimary,
            primaryContainer = accent.primaryContainer,
            onPrimaryContainer = accent.onPrimaryContainer,
            secondary = accent.secondary,
            onSecondary = accent.onSecondary,
            secondaryContainer = accent.secondaryContainer,
            onSecondaryContainer = accent.onSecondaryContainer,
            tertiary = accent.tertiary,
            onTertiary = accent.onTertiary,
            tertiaryContainer = accent.tertiaryContainer,
            onTertiaryContainer = accent.onTertiaryContainer,
        )
    }

    private fun createDarkColorScheme(accent: AccentColor): ColorScheme {
        return darkColorScheme(
            primary = accent.primaryDark,
            onPrimary = accent.onPrimaryDark,
            primaryContainer = accent.primaryContainerDark,
            onPrimaryContainer = accent.onPrimaryContainerDark,
            secondary = accent.secondaryDark,
            onSecondary = accent.onSecondaryDark,
            secondaryContainer = accent.secondaryContainerDark,
            onSecondaryContainer = accent.onSecondaryContainerDark,
            tertiary = accent.tertiaryDark,
            onTertiary = accent.onTertiaryDark,
            tertiaryContainer = accent.tertiaryContainerDark,
            onTertiaryContainer = accent.onTertiaryContainerDark,
        )
    }
}

private fun ColorScheme.toHighContrast(): ColorScheme {
    return copy(
        primary = if (primary.luminance() > 0.5f) Color.Black else Color.White,
        onPrimary = if (onPrimary.luminance() > 0.5f) Color.Black else Color.White,
        surface = if (surface.luminance() > 0.5f) Color.White else Color.Black,
        onSurface = if (onSurface.luminance() > 0.5f) Color.Black else Color.White,
    )
}

private fun Color.luminance(): Float {
    val r = if (red <= 0.03928f) red / 12.92f else ((red + 0.055f) / 1.055f).pow(2.4f)
    val g = if (green <= 0.03928f) green / 12.92f else ((green + 0.055f) / 1.055f).pow(2.4f)
    val b = if (blue <= 0.03928f) blue / 12.92f else ((blue + 0.055f) / 1.055f).pow(2.4f)
    return 0.2126f * r + 0.7152f * g + 0.0722f * b
}

enum class AccentColor(
    val displayName: String,
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    // Dark theme variants
    val primaryDark: Color,
    val onPrimaryDark: Color,
    val primaryContainerDark: Color,
    val onPrimaryContainerDark: Color,
    val secondaryDark: Color,
    val onSecondaryDark: Color,
    val secondaryContainerDark: Color,
    val onSecondaryContainerDark: Color,
    val tertiaryDark: Color,
    val onTertiaryDark: Color,
    val tertiaryContainerDark: Color,
    val onTertiaryContainerDark: Color,
) {
    PURPLE(
        displayName = "Purple",
        primary = Color(0xFF6750A4),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFE9DDFF),
        onPrimaryContainer = Color(0xFF22005D),
        secondary = Color(0xFF625B71),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE8DEF8),
        onSecondaryContainer = Color(0xFF1E192B),
        tertiary = Color(0xFF7E5260),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFFFD9E3),
        onTertiaryContainer = Color(0xFF31101D),
        primaryDark = Color(0xFFCFBCFF),
        onPrimaryDark = Color(0xFF381E72),
        primaryContainerDark = Color(0xFF4F378A),
        onPrimaryContainerDark = Color(0xFFE9DDFF),
        secondaryDark = Color(0xFFCBC2DB),
        onSecondaryDark = Color(0xFF332D41),
        secondaryContainerDark = Color(0xFF4A4458),
        onSecondaryContainerDark = Color(0xFFE8DEF8),
        tertiaryDark = Color(0xFFEFB8C8),
        onTertiaryDark = Color(0xFF4A2532),
        tertiaryContainerDark = Color(0xFF633B48),
        onTertiaryContainerDark = Color(0xFFFFD9E3),
    ),

    BLUE(
        displayName = "Blue",
        primary = Color(0xFF0061A4),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFD1E4FF),
        onPrimaryContainer = Color(0xFF001D36),
        secondary = Color(0xFF535F70),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFD7E3F8),
        onSecondaryContainer = Color(0xFF101C2B),
        tertiary = Color(0xFF6B5778),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFF2DAFF),
        onTertiaryContainer = Color(0xFF251432),
        primaryDark = Color(0xFF9ECAFF),
        onPrimaryDark = Color(0xFF003259),
        primaryContainerDark = Color(0xFF00497D),
        onPrimaryContainerDark = Color(0xFFD1E4FF),
        secondaryDark = Color(0xFFBBC7DB),
        onSecondaryDark = Color(0xFF253140),
        secondaryContainerDark = Color(0xFF3B4858),
        onSecondaryContainerDark = Color(0xFFD7E3F8),
        tertiaryDark = Color(0xFFD5BEE4),
        onTertiaryDark = Color(0xFF3B2948),
        tertiaryContainerDark = Color(0xFF523F5F),
        onTertiaryContainerDark = Color(0xFFF2DAFF),
    ),

    GREEN(
        displayName = "Green",
        primary = Color(0xFF006A60),
        onPrimary = Color.White,
        primaryContainer = Color(0xFF72F8E7),
        onPrimaryContainer = Color(0xFF00201C),
        secondary = Color(0xFF4A635F),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFCCE8E2),
        onSecondaryContainer = Color(0xFF051F1C),
        tertiary = Color(0xFF456179),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFC0E8FF),
        onTertiaryContainer = Color(0xFF001E30),
        primaryDark = Color(0xFF54DBCB),
        onPrimaryDark = Color(0xFF003731),
        primaryContainerDark = Color(0xFF005048),
        onPrimaryContainerDark = Color(0xFF72F8E7),
        secondaryDark = Color(0xFFB0CCC6),
        onSecondaryDark = Color(0xFF1C3531),
        secondaryContainerDark = Color(0xFF324B47),
        onSecondaryContainerDark = Color(0xFFCCE8E2),
        tertiaryDark = Color(0xFFA2CCEB),
        onTertiaryDark = Color(0xFF0A3446),
        tertiaryContainerDark = Color(0xFF2B4A62),
        onTertiaryContainerDark = Color(0xFFC0E8FF),
    )
}