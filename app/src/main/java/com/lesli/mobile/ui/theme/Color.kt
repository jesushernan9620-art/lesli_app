package com.lesli.mobile.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Cosmic Palette for Lesli Mobile
val CosmicBackground = Color(0xFF080B18)
val CosmicSurface = Color(0xFF11152A)
val CosmicCardSurface = Color(0xFF181E38)

val LesliPurple = Color(0xFF782BFF) // Primary Morado
val LesliViolet = Color(0xFFB15CFF) // Secondary Violeta
val LesliCyan = Color(0xFF42D9FF)   // Accent / Tertiary Cian

val TextPrimary = Color(0xFFF4F0FF)
val TextSecondary = Color(0xFFA6A4BC)
val BorderColor = Color(0xFF252D4D)

// Light theme fallback palette
val LightBackground = Color(0xFFF6F4FF)
val LightSurface = Color(0xFFFFFFFF)
val LightCardSurface = Color(0xFFEDE8FC)
val LightTextPrimary = Color(0xFF080B18)
val LightTextSecondary = Color(0xFF5A5775)
val LightBorderColor = Color(0xFFD6CFF7)

// Priority & Status Colors
val PriorityHigh = Color(0xFFFF4E6E)
val PriorityMedium = Color(0xFFFFAB3D)
val PriorityLow = Color(0xFF42D9FF)
val TaskCompleted = Color(0xFF42D9FF)
val ErrorRed = Color(0xFFFF3B5C)

// Cosmic Gradients
val CosmicGradientBrush = Brush.horizontalGradient(
    colors = listOf(LesliPurple, LesliViolet, LesliCyan)
)

val CosmicCardGradientBrush = Brush.linearGradient(
    colors = listOf(CosmicCardSurface, Color(0xFF1E2445))
)
