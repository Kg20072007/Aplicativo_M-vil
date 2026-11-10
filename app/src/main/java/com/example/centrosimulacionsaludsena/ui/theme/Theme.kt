package com.example.centrosimulacionsaludsena.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta para el Modo Oscuro (SENA Moderno)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CD200),      // Verde SENA brillante
    secondary = Color(0xFF38BDF8),    // Azul claro institucional
    background = Color(0xFF0F172A),   // Fondo oscuro profundo
    surface = Color(0xFF1E293B),      // Tarjetas oscuras
    onPrimary = Color.Black,
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC)
)

// Paleta para el Modo Claro (SENA Institucional)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF39A900),      // Verde SENA principal
    secondary = Color(0xFF00324D),    // Azul SENA institucional
    background = Color(0xFFF8FAFC),   // Fondo claro suave
    surface = Color(0xFFFFFFFF),      // Tarjetas blancas
    onPrimary = Color.White,
    onBackground = Color(0xFF1E293B),
    onSurface = Color(0xFF1E293B)
)

@Composable
fun CentroSimulacionSaludSenaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color está disponible en Android 12+
    dynamicColor: Boolean = true,
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}