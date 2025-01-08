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
package io.androidpoet.drafter.gant

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.max

public class GanttChartRenderer(
  public val data: GanttChartData,
) {
  public fun calculateMaxValues(): Pair<Float, Float> {
    // Find the max end month among all tasks
    val maxMonth = data.tasks.maxOfOrNull { it.startMonth + it.duration } ?: 1f
    return Pair(max(maxMonth, 1f), max(data.tasks.size.toFloat(), 1f))
  }

  public fun drawTasks(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxMonth: Float,
    animationProgress: Float,
  ) {
    if (data.tasks.isEmpty()) return

    // Avoid dividing by zero
    val safeMaxMonth = max(maxMonth, 1f)
    // Each task row gets an equal portion of the chart height
    val taskHeight = max(chartHeight / data.tasks.size, 1f)

    data.tasks.forEachIndexed { index, task ->
      // Compute the bar's X position and width
      val startX = chartLeft + (task.startMonth / safeMaxMonth) * chartWidth
      val width = max((task.duration / safeMaxMonth) * chartWidth * animationProgress, 1f)
      val y = chartTop + index * taskHeight

      // Pick a color; default to Blue if index is out of range
      val color = if (index < data.taskColors.size) {
        data.taskColors[index]
      } else {
        Color.Blue
      }

      // Draw a rectangle representing this task
      drawScope.drawRect(
        color = color.copy(alpha = animationProgress),
        topLeft = Offset(startX, y + taskHeight * 0.1f),
        size = Size(width, max(taskHeight * 0.8f, 1f)),
      )
    }
  }
}
