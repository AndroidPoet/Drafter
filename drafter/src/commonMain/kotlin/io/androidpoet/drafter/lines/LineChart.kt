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
package io.androidpoet.drafter.lines

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

@Composable
public fun LineChart(
  renderer: LineChartDataRenderer,
  modifier: Modifier = Modifier,
  isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
) {
  val textMeasurer = rememberTextMeasurer()
  val animationProgress =
    remember {
      Animatable(0f)
    }

  LaunchedEffect(Unit) {
    animationProgress.animateTo(
      targetValue = 1f,
      animationSpec =
      tween(
        durationMillis = 3000, // Increased duration
        easing = FastOutSlowInEasing,
      ),
    )
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val chartHeight = size.height * 0.8f
    val chartWidth = size.width * 0.8f
    val chartTop = size.height * 0.1f
    val chartBottom = chartTop + chartHeight
    val chartLeft = size.width * 0.1f

    val maxValue = renderer.calculateMaxValue()

    drawAxes(chartLeft, chartTop, chartBottom, chartWidth, isSystemInDarkTheme)
    drawYAxisLabels(textMeasurer, chartLeft, chartTop, chartBottom, maxValue, isSystemInDarkTheme)

    // Animate with FastOutSlowInEasing for smoother appearance
    val currentProgress = FastOutSlowInEasing.transform(animationProgress.value)

    renderer.drawLines(
      drawScope = this,
      chartLeft = chartLeft,
      chartTop = chartTop,
      chartWidth = chartWidth,
      chartHeight = chartHeight,
      maxValue = maxValue,
      animationProgress = currentProgress,
    )

    renderer.getLabels().forEachIndexed { index, label ->
      val x = chartLeft + index * (chartWidth / (renderer.getLabels().size - 1))
      drawXAxisLabel(textMeasurer, label, x, chartBottom, isSystemInDarkTheme)
    }
  }
}

private fun DrawScope.drawXAxisLabel(
  textMeasurer: TextMeasurer,
  label: String,
  x: Float,
  y: Float,
  isSystemInDarkTheme: Boolean,
) {
  val style =
    TextStyle(fontSize = 10.sp, color = if (isSystemInDarkTheme) Color.White else Color.Black)
  val textLayoutResult = textMeasurer.measure(label, style)
  drawText(
    textMeasurer = textMeasurer,
    text = label,
    style = style,
    topLeft = Offset(x - textLayoutResult.size.width / 2, y + 5f),
  )
}

private fun DrawScope.drawAxes(
  left: Float,
  top: Float,
  bottom: Float,
  width: Float,
  isSystemInDarkTheme: Boolean,
) {
  drawLine(
    if (isSystemInDarkTheme) Color.White else Color.Black,
    Offset(left, top),
    Offset(left, bottom),
    strokeWidth = 2f,
  )
  drawLine(
    if (isSystemInDarkTheme) Color.White else Color.Black,
    Offset(left, bottom),
    Offset(left + width, bottom),
    strokeWidth = 2f,
  )
}

private fun DrawScope.drawYAxisLabels(
  textMeasurer: TextMeasurer,
  left: Float,
  top: Float,
  bottom: Float,
  maxValue: Float,
  isSystemInDarkTheme: Boolean,
) {
  val style =
    TextStyle(fontSize = 10.sp, color = if (isSystemInDarkTheme) Color.White else Color.Black)

  // Calculate a nice step size based on the max value
  val step = calculateGridStep(maxValue)
  val numSteps = (maxValue / step).toInt()

  // Draw labels for each step
  for (i in 0..numSteps) {
    val value = i * step
    val ratio = value / maxValue
    val y = bottom - (ratio * (bottom - top))
    val label = value.toInt().toString()

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

private fun calculateGridStep(maxValue: Float): Float {
  val magnitude = floor(log10(maxValue.toDouble())).toFloat()
  val baseStep = 10.0f.pow(magnitude)

  return when {
    maxValue / baseStep > 5 -> baseStep * 2
    maxValue / baseStep > 2 -> baseStep
    else -> baseStep / 2
  }
}
