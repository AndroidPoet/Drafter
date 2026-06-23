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
package io.androidpoet.drafter.lines.renderer

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.androidpoet.drafter.internal.drawSmoothLine
import io.androidpoet.drafter.internal.drawVertexDot
import io.androidpoet.drafter.lines.LineChartDataRenderer
import io.androidpoet.drafter.lines.model.GroupedLineChartData

@Immutable
public class GroupedLineChartRenderer(
  private val data: GroupedLineChartData,
) : LineChartDataRenderer {
  override fun getLabels(): List<String> = data.labels

  override fun calculateMaxValue(): Float = data.groupedValues.flatten().maxOrNull() ?: 0f

  override fun drawLines(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  ) {
    val numPoints = data.labels.size
    if (numPoints < 2 || maxValue <= 0f) return
    val baseline = chartTop + chartHeight
    val xPositions =
      List(numPoints) { index ->
        chartLeft + index * (chartWidth / (numPoints - 1))
      }

    data.itemNames.forEachIndexed { itemIndex, _ ->
      val color = data.colors.getOrElse(itemIndex) { Color.Gray }
      val points =
        List(numPoints) { index ->
          val value = data.groupedValues[index][itemIndex]
          val x = xPositions[index]
          val y = baseline - (value / maxValue) * chartHeight
          Offset(x, y)
        }

      // Smooth, multi-series lines with no fill so overlapping series stay legible.
      drawScope.drawSmoothLine(
        points = points,
        color = color,
        baseline = baseline,
        progress = animationProgress,
        strokeWidth = 5f,
        fill = false,
        endDot = false,
      )

      // Reveal the vertex dots in step with the line.
      val span = xPositions.last() - xPositions.first()
      val revealRight = xPositions.first() + span * animationProgress.coerceIn(0f, 1f)
      points.forEach { p ->
        if (p.x <= revealRight + 0.5f) drawScope.drawVertexDot(p, color, radius = 5f)
      }
    }
  }
}
