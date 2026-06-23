package io.androidpoet.drafter.stepline

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

@Composable
public fun StepLineChart(
  renderer: StepLineChartRenderer,
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
    val chartLeft = size.width * 0.1f
    val chartTop = size.height * 0.1f
    val chartWidth = size.width * 0.8f
    val chartHeight = size.height * 0.8f
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
