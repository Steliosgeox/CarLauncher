package com.steliosgeox.carlauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NightColorScheme = darkColorScheme(
    background = NightBackground,
    surface = NightSurface,
    surfaceVariant = NightSurfaceVariant,
    primary = NightPrimary,
    onSurface = NightOnSurface,
    onSurfaceVariant = NightOnSurfaceVariant,
    onBackground = NightOnSurface
)

private val DayColorScheme = darkColorScheme(
    background = DayBackground,
    surface = DaySurface,
    surfaceVariant = DaySurfaceVariant,
    primary = DayPrimary,
    onSurface = DayOnSurface,
    onSurfaceVariant = DayOnSurfaceVariant,
    onBackground = DayOnSurface
)

private val SportColorScheme = darkColorScheme(
    background = SportBackground,
    surface = SportSurface,
    surfaceVariant = SportSurfaceVariant,
    primary = SportPrimary,
    onSurface = SportOnSurface,
    onSurfaceVariant = SportOnSurfaceVariant,
    onBackground = SportOnSurface
)

/**
 * Root theme for the CarLauncher app.
 * Always dark — no light mode, no dynamic colors.
 * The color scheme switches based on [themeMode].
 */
@Composable
fun CarLauncherTheme(
    themeMode: ThemeMode = ThemeMode.NIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        ThemeMode.NIGHT -> NightColorScheme
        ThemeMode.DAY   -> DayColorScheme
        ThemeMode.SPORT -> SportColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CarLauncherTypography,
        content = content
    )
}
