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

public interface LineChartRenderer<T : LineChartData> {
  public fun calculateMaxValue(data: T): Float

  public fun drawLines(
    drawScope: DrawScope,
    data: T,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  )
}

@Composable
public fun <T : LineChartData> LineChart(
  data: T,
  renderer: LineChartRenderer<T>,
  modifier: Modifier = Modifier,
) {
  val textMeasurer = rememberTextMeasurer()

  val animationProgress = remember { Animatable(0f) }

  // Animate the line drawing just like in the BarChart
  LaunchedEffect(Unit) {
    animationProgress.animateTo(
      targetValue = 1f,
      animationSpec =
      tween(
        durationMillis = 1000,
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

    val maxValue = renderer.calculateMaxValue(data)

    drawAxes(chartLeft, chartTop, chartBottom, chartWidth)
    drawYAxisLabels(textMeasurer, chartLeft, chartTop, chartBottom, maxValue)

    renderer.drawLines(
      drawScope = this,
      data = data,
      chartLeft = chartLeft,
      chartTop = chartTop,
      chartWidth = chartWidth,
      chartHeight = chartHeight,
      maxValue = maxValue,
      animationProgress = animationProgress.value,
    )

    data.labels.forEachIndexed { index, label ->
      val x = chartLeft + index * (chartWidth / (data.labels.size - 1))
      drawXAxisLabel(textMeasurer, label, x, chartBottom)
    }
  }
}

private fun DrawScope.drawXAxisLabel(
  textMeasurer: TextMeasurer,
  label: String,
  x: Float,
  y: Float,
) {
  val style = TextStyle(fontSize = 10.sp, color = Color.Black)
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
) {
  drawLine(Color.Black, Offset(left, top), Offset(left, bottom), strokeWidth = 2f)
  drawLine(Color.Black, Offset(left, bottom), Offset(left + width, bottom), strokeWidth = 2f)
}

private fun DrawScope.drawYAxisLabels(
  textMeasurer: TextMeasurer,
  left: Float,
  top: Float,
  bottom: Float,
  maxValue: Float,
) {
  val style = TextStyle(fontSize = 10.sp, color = Color.Black)
  (0..4).forEach { i ->
    val y = bottom - (i * (bottom - top) / 4f)
    val label = "${(maxValue * i / 4).toInt()}"
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
