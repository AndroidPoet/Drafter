package io.androidpoet.drafter.sunburst

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import kotlin.math.min

@Composable
public fun SunburstChart(
  renderer: SunburstChartRenderer,
  modifier: Modifier = Modifier,
  isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
  animate: Boolean = true,
) {
  val textMeasurer = rememberTextMeasurer()
  val progress = remember { Animatable(0f) }
  LaunchedEffect(animate) {
    if (animate) progress.animateTo(1f, tween(900)) else progress.snapTo(1f)
  }
  Canvas(modifier = modifier.fillMaxSize()) {
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val maxRadius = min(size.width, size.height) / 2f * 0.92f
    renderer.draw(
      drawScope = this,
      centerX = centerX,
      centerY = centerY,
      maxRadius = maxRadius,
      animationProgress = progress.value,
      isSystemInDarkTheme = isSystemInDarkTheme,
      textMeasurer = textMeasurer,
    )
  }
}
