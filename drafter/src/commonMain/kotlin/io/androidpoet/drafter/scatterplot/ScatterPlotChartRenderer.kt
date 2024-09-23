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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp

public interface ScatterPlotRenderer {
  public fun calculateMaxValues(data: ScatterPlotData): Pair<Float, Float>

  public fun drawPoints(
    drawScope: DrawScope,
    data: ScatterPlotData,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxX: Float,
    maxY: Float,
    animationProgress: Float,
  )
}

public class SimpleScatterPlotRenderer : ScatterPlotRenderer {
  override fun calculateMaxValues(data: ScatterPlotData): Pair<Float, Float> {
    val maxX = data.points.maxOfOrNull { it.first } ?: 0f
    val maxY = data.points.maxOfOrNull { it.second } ?: 0f
    return Pair(maxX, maxY)
  }

  override fun drawPoints(
    drawScope: DrawScope,
    data: ScatterPlotData,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxX: Float,
    maxY: Float,
    animationProgress: Float,
  ) {
    data.points.forEachIndexed { index, point ->
      val x = chartLeft + (point.first / maxX) * chartWidth
      val y = chartTop + chartHeight - (point.second / maxY) * chartHeight

      val pointSize = 5f * animationProgress

      val color = if (index < data.pointColors.size) data.pointColors[index] else Color.Black
      drawScope.drawCircle(
        color = color.copy(alpha = animationProgress),
        radius = pointSize,
        center = Offset(x, y),
      )
    }
  }
}

public fun DrawScope.drawAxes(
  left: Float,
  top: Float,
  bottom: Float,
  width: Float,
) {
  drawLine(Color.Black, Offset(left, top), Offset(left, bottom), strokeWidth = 2f)
  drawLine(Color.Black, Offset(left, bottom), Offset(left + width, bottom), strokeWidth = 2f)
}

public fun DrawScope.drawYAxisLabels(
  textMeasurer: TextMeasurer,
  left: Float,
  top: Float,
  bottom: Float,
  maxY: Float,
) {
  val style = TextStyle(fontSize = 10.sp, color = Color.Black)
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

public fun DrawScope.drawXAxisLabels(
  textMeasurer: TextMeasurer,
  left: Float,
  bottom: Float,
  width: Float,
  maxX: Float,
) {
  val style = TextStyle(fontSize = 10.sp, color = Color.Black)
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
