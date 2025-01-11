/*
 * Designed and developed by 2024 androidpoet (Ranbir Singh)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.androidpoet.drafter.scatterplot

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp

@Composable
public fun ScatterPlot(
  renderer: ScatterPlotRenderer,
  modifier: Modifier = Modifier,
  isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
) {
  val textMeasurer = rememberTextMeasurer()
  val animationProgress = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    animationProgress.animateTo(
      targetValue = 1f,
      animationSpec =
      tween(
        durationMillis = 2000,
        easing = LinearOutSlowInEasing,
      ),
    )
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val chartHeight = size.height * 0.8f
    val chartWidth = size.width * 0.8f
    val chartTop = size.height * 0.1f
    val chartBottom = chartTop + chartHeight
    val chartLeft = size.width * 0.1f

    val (maxX, maxY) = renderer.calculateMaxValues()

    drawAxes(chartLeft, chartTop, chartBottom, chartWidth, isSystemInDarkTheme)
    drawYAxisLabels(textMeasurer, chartLeft, chartTop, chartBottom, maxY, isSystemInDarkTheme)
    drawXAxisLabels(textMeasurer, chartLeft, chartBottom, chartWidth, maxX, isSystemInDarkTheme)

    renderer.drawPoints(
      drawScope = this,
      chartLeft = chartLeft,
      chartTop = chartTop,
      chartWidth = chartWidth,
      chartHeight = chartHeight,
      maxX = maxX,
      maxY = maxY,
      animationProgress = animationProgress.value,
    )
  }
}

internal fun DrawScope.drawYAxisLabels(
  textMeasurer: TextMeasurer,
  left: Float,
  top: Float,
  bottom: Float,
  maxY: Float,
  isSystemInDarkTheme: Boolean,
) {
  val style =
    TextStyle(fontSize = 10.sp, color = if (isSystemInDarkTheme) Color.White else Color.Black)
  (0..4).forEach { i ->
    val y = bottom - (i * (bottom - top) / 4f)
    val label = "${(maxY * i / 4)}"
    val textLayoutResult = textMeasurer.measure(label, style)
    drawText(
      textMeasurer = textMeasurer,
      text = label,
      style = style,
      topLeft =
      Offset(
        left - textLayoutResult.size.width - 5f,
        y - textLayoutResult.size.height / 2,
      ),
    )
  }
}

internal fun DrawScope.drawXAxisLabels(
  textMeasurer: TextMeasurer,
  left: Float,
  bottom: Float,
  width: Float,
  maxX: Float,
  isSystemInDarkTheme: Boolean,
) {
  val style =
    TextStyle(fontSize = 10.sp, color = if (isSystemInDarkTheme) Color.White else Color.Black)
  (0..4).forEach { i ->
    val x = left + (i * width / 4f)
    val label = "${(maxX * i / 4)}"
    val textLayoutResult = textMeasurer.measure(label, style)
    drawText(
      textMeasurer = textMeasurer,
      text = label,
      style = style,
      topLeft =
      Offset(
        x - textLayoutResult.size.width / 2,
        bottom + 5f,
      ),
    )
  }
}
