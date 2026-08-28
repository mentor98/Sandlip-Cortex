package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CortexDarkColorScheme = darkColorScheme(
  primary = CortexPrimary,
  onPrimary = Color.White,
  primaryContainer = CortexSurfaceElevated,
  onPrimaryContainer = CortexCyan,
  secondary = CortexCyan,
  onSecondary = CortexDeepVoid,
  secondaryContainer = CortexSurfaceVariant,
  onSecondaryContainer = CortexTextPrimary,
  tertiary = CortexEmerald,
  onTertiary = CortexDeepVoid,
  tertiaryContainer = CortexSurfaceElevated,
  onTertiaryContainer = CortexEmerald,
  background = CortexDeepVoid,
  onBackground = CortexTextPrimary,
  surface = CortexSurface,
  onSurface = CortexTextPrimary,
  surfaceVariant = CortexSurfaceVariant,
  onSurfaceVariant = CortexTextSecondary,
  error = CortexRose,
  onError = Color.White,
  outline = CortexBorder,
  outlineVariant = CortexBorderGlow
)

@Composable
fun SandlipCortexTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = CortexDarkColorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  SandlipCortexTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
