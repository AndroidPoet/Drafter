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
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

public interface GanttChartRenderer {
  public fun calculateMaxValues(data: GanttChartData): Pair<Float, Float>

  public fun drawTasks(
    drawScope: DrawScope,
    data: GanttChartData,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxMonth: Float,
    animationProgress: Float,
  )
}

public class SimpleGanttChartRenderer : GanttChartRenderer {
  override fun calculateMaxValues(data: GanttChartData): Pair<Float, Float> {
    val maxMonth = data.tasks.maxOfOrNull { it.startMonth + it.duration } ?: 1f
    return Pair(max(maxMonth, 1f), max(data.tasks.size.toFloat(), 1f))
  }

  override fun drawTasks(
    drawScope: DrawScope,
    data: GanttChartData,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxMonth: Float,
    animationProgress: Float,
  ) {
    if (data.tasks.isEmpty()) return

    val safeMaxMonth = max(maxMonth, 1f)
    val taskHeight = max(chartHeight / data.tasks.size, 1f)

    data.tasks.forEachIndexed { index, task ->
      val startX = chartLeft + (task.startMonth / safeMaxMonth) * chartWidth
      val width = max((task.duration / safeMaxMonth) * chartWidth * animationProgress, 1f)
      val y = chartTop + index * taskHeight

      val color = if (index < data.taskColors.size) data.taskColors[index] else Color.Blue
      drawScope.drawRect(
        color = color.copy(alpha = animationProgress),
        topLeft = Offset(startX, y + taskHeight * 0.1f),
        size = Size(width, max(taskHeight * 0.8f, 1f)),
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
  tasks: List<GanttTask>,
) {
  if (tasks.isEmpty()) return

  val style = TextStyle(fontSize = 10.sp, color = Color.Black)
  val taskHeight = max((bottom - top) / tasks.size, 1f)

  tasks.forEachIndexed { index, task ->
    val y = top + index * taskHeight + taskHeight / 2
    val textLayoutResult = textMeasurer.measure(task.name, style)
    drawText(
      textMeasurer = textMeasurer,
      text = task.name,
      style = style,
      topLeft = Offset(
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
  maxMonth: Float,
  canvasHeight: Float, // Add canvas height as a parameter
) {
  val style = TextStyle(fontSize = 10.sp, color = Color.Black)
  val safeMaxMonth = max(maxMonth, 1f)

  (0..4).forEach { i ->
    val x = left + (i * width / 4f)
    val label = "${(safeMaxMonth * i / 4).toInt()}"
    val textLayoutResult = textMeasurer.measure(label, style)

    // Ensure the y-coordinate is within the canvas bounds
    val safeBottom = min(bottom + 5f, canvasHeight - textLayoutResult.size.height)

    drawText(
      textMeasurer = textMeasurer,
      text = label,
      style = style,
      topLeft = Offset(
        x - textLayoutResult.size.width / 2,
        safeBottom,
      ),
    )
  }
}
