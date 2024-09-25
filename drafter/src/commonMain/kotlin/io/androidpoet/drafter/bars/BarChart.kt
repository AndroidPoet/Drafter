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
package io.androidpoet.drafter.bars

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp

public interface BarChartData {
  public val labels: List<String>
}

public interface BarChartRenderer<T : BarChartData> {
  public fun calculateMaxValue(data: T): Float

  public fun calculateBarAndSpacing(
    chartWidth: Float,
    dataSize: Int,
    barsPerGroup: Int,
  ): Pair<Float, Float>

  public fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float

  public fun drawBars(
    drawScope: DrawScope,
    data: T,
    index: Int,
    left: Float,
    barWidth: Float,
    groupSpacing: Float,
    chartBottom: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  )
}

@Composable
public fun <T : BarChartData> BarChart(
  data: T,
  renderer: BarChartRenderer<T>,
  modifier: Modifier = Modifier,
  animate: Boolean = true,
) {
  val textMeasurer = rememberTextMeasurer()
  val animationProgress = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    animationProgress.animateTo(
      targetValue = 1f,
      animationSpec =
      tween(
        durationMillis = 1000,
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

    val maxValue = renderer.calculateMaxValue(data)
    val barsPerGroup =
      when (data) {
        is GroupedBarChartData -> data.itemNames.size
        is StackedBarChartData -> 1
        is WaterfallChartData -> 1 // Add this case
        else -> 1
      }

    val (barWidth, groupSpacing) =
      renderer.calculateBarAndSpacing(
        chartWidth,
        data.labels.size,
        barsPerGroup,
      )

    drawAxes(chartLeft, chartTop, chartBottom, chartWidth)
    drawYAxisLabels(textMeasurer, chartLeft, chartTop, chartBottom, maxValue)

    var currentLeft = chartLeft

    data.labels.forEachIndexed { index, label ->
      // Draw bars
      renderer.drawBars(
        drawScope = this,
        data = data,
        index = index,
        left = currentLeft,
        barWidth = barWidth,
        groupSpacing = groupSpacing,
        chartBottom = chartBottom,
        chartHeight = chartHeight,
        maxValue = maxValue,
        animationProgress = animationProgress.value,
      )

      val groupWidth = renderer.calculateGroupWidth(barWidth, barsPerGroup)
      val labelX = currentLeft + groupWidth / 2

      drawXAxisLabel(textMeasurer, label, labelX, chartBottom)

      currentLeft += groupWidth + groupSpacing
    }
  }
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
