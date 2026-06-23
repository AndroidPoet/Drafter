package io.androidpoet.drafter.treemap

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

@Composable
public fun TreemapChart(
  renderer: TreemapChartRenderer,
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
    // Small inset so tiles don't bleed to the very edge.
    val inset = minOf(size.width, size.height) * 0.04f
    renderer.draw(
      drawScope = this,
      chartLeft = inset,
      chartTop = inset,
      chartWidth = size.width - inset * 2f,
      chartHeight = size.height - inset * 2f,
      animationProgress = progress.value,
      isSystemInDarkTheme = isSystemInDarkTheme,
      textMeasurer = textMeasurer,
    )
  }
}
