package com.yite.senaoapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        secondary = secondaryDark,
        tertiary = tertiaryDark,
        background = backgroundDark,
        onSurface = onSurfaceDark,
        onSurfaceVariant = onSurfaceVariantDark,
        surfaceContainer = surfaceContainerDark,
        error = errorDark,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        secondary = secondaryLight,
        tertiary = tertiaryLight,
        background = backgroundLight,
        onSurface = onSurfaceLight,
        onSurfaceVariant = onSurfaceVariantLight,
        surfaceContainer = surfaceContainerLight,
        error = errorLight,
    )

@Composable
fun SenaoInterviewDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
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
        content = content,
    )
}
