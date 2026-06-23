package io.androidpoet.drafter.sankey

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

/**
 * A Sankey flow diagram: layered node bars connected by smooth gradient bands
 * whose thickness encodes each flow's value. Bands reveal left→right and node
 * bars grow in height on first composition.
 *
 * @param renderer holds the [io.androidpoet.drafter.sankey.model.SankeyData] and layout/draw logic
 * @param animate when true, plays the reveal animation; otherwise the chart snaps fully drawn
 */
@Composable
public fun SankeyChart(
  renderer: SankeyChartRenderer,
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
    // 8% inset on each side for node labels.
    val inset = 0.08f
    val chartLeft = size.width * inset
    val chartTop = size.height * inset
    val chartWidth = size.width * (1f - inset * 2f)
    val chartHeight = size.height * (1f - inset * 2f)

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
