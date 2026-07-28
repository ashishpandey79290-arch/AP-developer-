package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TaskPrimary,
    onPrimary = TaskOnPrimary,
    primaryContainer = TaskPrimaryContainer,
    onPrimaryContainer = TaskOnPrimaryContainer,
    secondary = TaskSecondary,
    onSecondary = TaskOnSecondary,
    secondaryContainer = TaskSecondaryContainer,
    onSecondaryContainer = TaskOnSecondaryContainer,
    tertiary = TaskTertiary,
    onTertiary = TaskOnTertiary,
    tertiaryContainer = TaskTertiaryContainer,
    onTertiaryContainer = TaskOnTertiaryContainer,
    background = TaskBackground,
    onBackground = TaskOnBackground,
    surface = TaskSurface,
    onSurface = TaskOnSurface,
    onSurfaceVariant = TaskOnSurfaceVariant,
    surfaceContainerLowest = TaskSurfaceContainerLowest,
    surfaceContainerLow = TaskSurfaceContainerLow,
    surfaceContainer = TaskSurfaceContainer,
    surfaceContainerHigh = TaskSurfaceContainerHigh,
    outline = TaskOutline,
    outlineVariant = TaskOutlineVariant,
    error = TaskError,
    onError = TaskOnError,
    errorContainer = TaskErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB0C6FF),
    onPrimary = Color(0xFF001945),
    primaryContainer = TaskPrimary,
    onPrimaryContainer = TaskOnPrimaryContainer,
    secondary = Color(0xFFB7C8E1),
    onSecondary = Color(0xFF0B1C30),
    secondaryContainer = TaskSecondary,
    onSecondaryContainer = Color(0xFFD3E4FE),
    tertiary = Color(0xFF6BD8CB),
    onTertiary = Color(0xFF00201D),
    tertiaryContainer = TaskTertiary,
    onTertiaryContainer = Color(0xFF89F5E7),
    background = Color(0xFF191C1E),
    onBackground = Color(0xFFE0E3E5),
    surface = Color(0xFF191C1E),
    onSurface = Color(0xFFE0E3E5),
    onSurfaceVariant = Color(0xFFC3C6D5),
    surfaceContainerLowest = Color(0xFF0E1113),
    surfaceContainerLow = Color(0xFF1D2022),
    surfaceContainer = Color(0xFF222527),
    surfaceContainerHigh = Color(0xFF2C2F31),
    outline = Color(0xFF8D919F),
    outlineVariant = Color(0xFF434653),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun TaskSecureTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    TaskSecureTheme(darkTheme = darkTheme, content = content)
}

