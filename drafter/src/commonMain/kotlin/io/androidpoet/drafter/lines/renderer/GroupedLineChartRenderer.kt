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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.androidpoet.drafter.lines.LineChartDataRenderer
import io.androidpoet.drafter.lines.model.GroupedLineChartData
import kotlin.math.pow

public class GroupedLineChartRenderer(
  private val data: GroupedLineChartData,
) : LineChartDataRenderer {

  override fun getLabels(): List<String> = data.labels

  override fun calculateMaxValue(): Float =
    data.groupedValues.flatten().maxOrNull() ?: 0f

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
    val xPositions = List(numPoints) { index ->
      chartLeft + index * (chartWidth / (numPoints - 1))
    }

    data.itemNames.forEachIndexed { itemIndex, _ ->
      val points = List(numPoints) { index ->
        val value = data.groupedValues[index][itemIndex]
        val x = xPositions[index]
        val y = chartTop + chartHeight - (value / maxValue) * chartHeight
        Offset(x, y)
      }

      val totalLength = points.zipWithNext().sumOf { (start, end) ->
        val dx = end.x - start.x
        val dy = end.y - start.y
        kotlin.math.sqrt((dx * dx + dy * dy).toDouble())
      }.toFloat()

      var currentLength = 0f

      points.zipWithNext().forEach { (start, end) ->
        val segmentLength = kotlin.math.sqrt(
          (end.x - start.x).pow(2) + (end.y - start.y).pow(2),
        )
        val segmentProgress = (currentLength + segmentLength) / totalLength

        if (segmentProgress <= animationProgress) {
          drawScope.drawLine(
            color = data.colors.getOrElse(itemIndex) { Color.Gray },
            start = start,
            end = end,
            strokeWidth = 2f,
          )
        } else if (currentLength / totalLength <= animationProgress) {
          val remainingProgress =
            (animationProgress - currentLength / totalLength) / (segmentLength / totalLength)
          val partialEnd = Offset(
            x = start.x + (end.x - start.x) * remainingProgress,
            y = start.y + (end.y - start.y) * remainingProgress,
          )
          drawScope.drawLine(
            color = data.colors.getOrElse(itemIndex) { Color.Gray },
            start = start,
            end = partialEnd,
            strokeWidth = 2f,
          )
        }
        currentLength += segmentLength
      }
    }
  }
}
