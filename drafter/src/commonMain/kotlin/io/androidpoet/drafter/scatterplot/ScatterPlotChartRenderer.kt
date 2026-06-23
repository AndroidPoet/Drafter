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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import io.androidpoet.drafter.scatterplot.model.ScatterPlotData
import io.androidpoet.drafter.theme.DrafterColors

public interface ScatterPlotRenderer : io.androidpoet.drafter.core.ChartRenderer {
  public fun calculateMaxValues(): Pair<Float, Float>

  public fun getPoints(): List<Pair<Float, Float>>

  public fun drawPoints(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxX: Float,
    maxY: Float,
    animationProgress: Float,
  )
}

@Immutable
public class SimpleScatterPlotRenderer(
  public val data: ScatterPlotData,
) : ScatterPlotRenderer {
  override fun calculateMaxValues(): Pair<Float, Float> {
    val maxX = data.points.maxOfOrNull { it.first } ?: 0f
    val maxY = data.points.maxOfOrNull { it.second } ?: 0f
    return Pair(maxX, maxY)
  }

  override fun getPoints(): List<Pair<Float, Float>> = data.points

  override fun drawPoints(
    drawScope: DrawScope,
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

      val pointSize = 6f * animationProgress

      val color =
        if (index < data.pointColors.size) data.pointColors[index] else Color.Gray
      val center = Offset(x, y)
      // Soft translucent halo + a crisp ringed dot for a premium, glassy feel.
      drawScope.drawCircle(
        color = color.copy(alpha = 0.16f * animationProgress),
        radius = pointSize * 2f,
        center = center,
      )
      drawScope.drawCircle(
        color = color.copy(alpha = animationProgress),
        radius = pointSize,
        center = center,
      )
      drawScope.drawCircle(
        color = Color.White.copy(alpha = animationProgress),
        radius = pointSize,
        center = center,
        style = Stroke(width = 1.5f),
      )
    }
  }
}

public fun DrawScope.drawAxes(
  left: Float,
  top: Float,
  bottom: Float,
  width: Float,
  isSystemInDarkTheme: Boolean,
) {
  val axisColor = if (isSystemInDarkTheme) DrafterColors.GridDark else DrafterColors.GridLight
  drawLine(
    axisColor,
    Offset(left, top),
    Offset(left, bottom),
    strokeWidth = 1.5f,
  )
  drawLine(
    axisColor,
    Offset(left, bottom),
    Offset(left + width, bottom),
    strokeWidth = 1.5f,
  )
}
