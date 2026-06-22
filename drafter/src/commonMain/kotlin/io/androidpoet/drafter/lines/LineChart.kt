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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.theme.DrafterColors
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
        durationMillis = 1100,
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

    drawGridAndLabels(
      textMeasurer,
      chartLeft,
      chartTop,
      chartBottom,
      chartWidth,
      maxValue,
      isSystemInDarkTheme,
    )

    renderer.drawLines(
      drawScope = this,
      chartLeft = chartLeft,
      chartTop = chartTop,
      chartWidth = chartWidth,
      chartHeight = chartHeight,
      maxValue = maxValue,
      animationProgress = animationProgress.value,
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
    TextStyle(
      fontSize = 11.sp,
      color = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight,
    )
  val textLayoutResult = textMeasurer.measure(label, style)
  drawText(
    textMeasurer = textMeasurer,
    text = label,
    style = style,
    topLeft = Offset(x - textLayoutResult.size.width / 2, y + 10f),
  )
}

/**
 * Draws faint horizontal grid lines and muted Y-axis value labels. Replaces the
 * old hard black axis lines for a softer, dashboard-style look.
 */
private fun DrawScope.drawGridAndLabels(
  textMeasurer: TextMeasurer,
  left: Float,
  top: Float,
  bottom: Float,
  width: Float,
  maxValue: Float,
  isSystemInDarkTheme: Boolean,
) {
  if (maxValue <= 0f) return
  val gridColor = if (isSystemInDarkTheme) DrafterColors.GridDark else DrafterColors.GridLight
  val style =
    TextStyle(
      fontSize = 11.sp,
      color = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight,
    )
  val step = calculateGridStep(maxValue)
  val numSteps = (maxValue / step).toInt()
  for (i in 0..numSteps) {
    val value = i * step
    val ratio = value / maxValue
    val y = bottom - (ratio * (bottom - top))

    drawLine(
      color = gridColor,
      start = Offset(left, y),
      end = Offset(left + width, y),
      strokeWidth = 1f,
    )

    val label = value.toInt().toString()
    val textLayoutResult = textMeasurer.measure(label, style)
    drawText(
      textMeasurer = textMeasurer,
      text = label,
      style = style,
      topLeft =
      Offset(
        left - textLayoutResult.size.width - 10f,
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
