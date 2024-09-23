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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill

public class StackedLineChartRenderer : LineChartRenderer<StackedLineChartData> {
  override fun calculateMaxValue(data: StackedLineChartData): Float =
    data.stacks.map { it.sum() }.maxOrNull() ?: 0f

  override fun drawLines(
    drawScope: DrawScope,
    data: StackedLineChartData,
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

    // Initialize accumulated values to zero
    val accumulatedValues = MutableList(numPoints) { 0f }

    // Draw the areas from bottom to top
    val stackCount = data.stacks[0].size
    for (stackIndex in 0 until stackCount) {
      val previousAccumulatedValues = accumulatedValues.toList() // Copy current accumulated values

      // Update accumulated values with the current stack's values
      for (i in 0 until numPoints) {
        accumulatedValues[i] += data.stacks[i][stackIndex]
      }

      // Create upper and lower points for the area
      val upperPoints =
        List(numPoints) { i ->
          val x = xPositions[i]
          val y = chartTop + chartHeight - (accumulatedValues[i] / maxValue) * chartHeight
          Offset(x, y)
        }

      val lowerPoints =
        List(numPoints) { i ->
          val x = xPositions[i]
          val y = chartTop + chartHeight - (previousAccumulatedValues[i] / maxValue) * chartHeight
          Offset(x, y)
        }

      // Build the path for the area between previous and current accumulated values
      val path =
        Path().apply {
          moveTo(upperPoints.first().x, upperPoints.first().y)
          for (point in upperPoints.drop(1)) {
            lineTo(point.x, point.y)
          }
          for (point in lowerPoints.reversed()) {
            lineTo(point.x, point.y)
          }
          close()
        }

      // Draw the area with the corresponding color
      drawScope.drawPath(
        path = path,
        color = data.colors.getOrElse(stackIndex) { Color.Gray },
        style = Fill,
      )
    }
  }
}