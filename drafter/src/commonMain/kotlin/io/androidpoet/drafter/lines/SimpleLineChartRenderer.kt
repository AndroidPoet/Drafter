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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

public class SimpleLineChartRenderer : LineChartRenderer<SimpleLineChartData> {
  override fun calculateMaxValue(data: SimpleLineChartData): Float = data.values.maxOrNull() ?: 0f

  override fun drawLines(
    drawScope: DrawScope,
    data: SimpleLineChartData,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  ) {
    val points =
      data.values.mapIndexed { index, value ->
        val x = chartLeft + index * (chartWidth / (data.values.size - 1))
        val y = chartTop + chartHeight - (value / maxValue) * chartHeight
        Offset(x, y)
      }

// Calculate the number of points to draw based on animation progress
    val animatedPointsCount = (points.size * animationProgress).toInt().coerceAtLeast(2)

// Draw lines up to the animated point count
    for (i in 0 until animatedPointsCount - 1) {
      drawScope.drawLine(
        color = data.color,
        start = points[i],
        end = points[i + 1],
        strokeWidth = 2f,
      )
    }
  }
}
