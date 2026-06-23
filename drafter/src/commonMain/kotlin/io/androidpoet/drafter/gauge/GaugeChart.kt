package io.androidpoet.drafter.gauge

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
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
public fun GaugeChart(
  renderer: GaugeChartRenderer,
  modifier: Modifier = Modifier,
  isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
  animate: Boolean = true,
) {
  val textMeasurer = rememberTextMeasurer()
  val progress = remember { Animatable(0f) }

  LaunchedEffect(animate) {
    if (animate) {
      progress.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 900, easing = LinearOutSlowInEasing),
      )
    } else {
      progress.snapTo(1f)
    }
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val radius = min(size.width, size.height) / 2f * 0.82f

    renderer.draw(
      drawScope = this,
      centerX = centerX,
      centerY = centerY,
      radius = radius,
      animationProgress = progress.value,
      isSystemInDarkTheme = isSystemInDarkTheme,
      textMeasurer = textMeasurer,
    )
  }
}
