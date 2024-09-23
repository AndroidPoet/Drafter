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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

public class GroupedLineChartRenderer : LineChartRenderer<GroupedLineChartData> {
  override fun calculateMaxValue(data: GroupedLineChartData): Float =
    data.groupedValues.flatten().maxOrNull() ?: 0f

  override fun drawLines(
    drawScope: DrawScope,
    data: GroupedLineChartData,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxValue: Float,
  ) {
    val numPoints = data.labels.size
    val xPositions =
      List(numPoints) { index ->
        chartLeft + index * (chartWidth / (numPoints - 1))
      }

    data.itemNames.forEachIndexed { itemIndex, _ ->
      val points =
        List(numPoints) { index ->
          val value = data.groupedValues[index][itemIndex]
          val x = xPositions[index]
          val y = chartTop + chartHeight - (value / maxValue) * chartHeight
          Offset(x, y)
        }

      for (i in 0 until points.size - 1) {
        drawScope.drawLine(
          color = data.colors.getOrElse(itemIndex) { Color.Gray },
          start = points[i],
          end = points[i + 1],
          strokeWidth = 2f,
        )
      }
    }
  }
}
