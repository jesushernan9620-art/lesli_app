package com.lesli.mobile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lesli.mobile.R
import com.lesli.mobile.ui.theme.LesliCyan
import com.lesli.mobile.ui.theme.LesliPurple
import com.lesli.mobile.ui.theme.LesliViolet

@Composable
fun LesliMoonLogo(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    showGlow: Boolean = true
) {
    Box(
        modifier = modifier
            .size(size)
            .then(
                if (showGlow) {
                    Modifier.shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = LesliViolet,
                        ambientColor = LesliCyan
                    )
                } else Modifier
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        LesliViolet.copy(alpha = 0.35f),
                        LesliPurple.copy(alpha = 0.15f),
                        Color.Transparent
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_lesli_moon_logo),
            contentDescription = "Lesli Moon Logo",
            modifier = Modifier.size(size * 0.95f)
        )
    }
}
