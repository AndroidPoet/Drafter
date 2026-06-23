package io.androidpoet.drafter.funnel

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
public fun FunnelChart(
  renderer: FunnelChartRenderer,
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
    val chartLeft = size.width * 0.1f
    val chartTop = size.height * 0.08f
    val chartWidth = size.width * 0.8f
    val chartHeight = size.height * 0.84f

    renderer.draw(
      drawScope = this,
      chartLeft = chartLeft,
      chartTop = chartTop,
      chartWidth = chartWidth,
      chartHeight = chartHeight,
      animationProgress = progress.value,
      isSystemInDarkTheme = isSystemInDarkTheme,
      textMeasurer = textMeasurer,
    )
  }
}
