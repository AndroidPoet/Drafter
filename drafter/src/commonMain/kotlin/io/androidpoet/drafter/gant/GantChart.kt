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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

@Composable
public fun GanttChart(
  renderer: GanttChartRenderer,
  modifier: Modifier = Modifier,
) {
  val textMeasurer = rememberTextMeasurer()
  val animationProgress = remember { Animatable(0f) }

  // Animate on first composition
  LaunchedEffect(Unit) {
    animationProgress.animateTo(
      targetValue = 1f,
      animationSpec = tween(
        durationMillis = 2000,
        easing = LinearOutSlowInEasing,
      ),
    )
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    if (size.width < 1f || size.height < 1f) return@Canvas

    val chartHeight = size.height * 0.8f
    val chartWidth = size.width * 0.7f
    val chartTop = size.height * 0.1f
    val chartBottom = chartTop + chartHeight
    val chartLeft = size.width * 0.2f

    // 1) Get the maximum month from the renderer
    val (maxMonth, _) = renderer.calculateMaxValues()

    // 2) Draw axes
    drawAxes(
      left = chartLeft,
      top = chartTop,
      bottom = chartBottom,
      width = chartWidth,
    )

    // 3) Y-axis labels
    drawYAxisLabels(
      textMeasurer = textMeasurer,
      left = chartLeft,
      top = chartTop,
      bottom = chartBottom,
      tasks = renderer.data.tasks,
    )

    // 4) X-axis labels (pass tasks here)
    drawXAxisLabels(
      textMeasurer = textMeasurer,
      left = chartLeft,
      bottom = chartBottom,
      width = chartWidth,
      maxMonth = maxMonth,
      canvasHeight = size.height,
      tasks = renderer.data.tasks,
    )

    // 5) Draw tasks
    renderer.drawTasks(
      drawScope = this,
      chartLeft = chartLeft,
      chartTop = chartTop,
      chartWidth = chartWidth,
      chartHeight = chartHeight,
      maxMonth = maxMonth,
      animationProgress = animationProgress.value,
    )
  }
}

public fun DrawScope.drawAxes(
  left: Float,
  top: Float,
  bottom: Float,
  width: Float,
) {
  // Y-axis
  drawLine(Color.Black, Offset(left, top), Offset(left, bottom), strokeWidth = 2f)
  // X-axis
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
    val yCenter = top + index * taskHeight + taskHeight / 2
    val textLayoutResult = textMeasurer.measure(task.name, style)

    // Safely clamp X if you want to avoid negative draws
    val finalX = (left - textLayoutResult.size.width - 5f).coerceAtLeast(0f)

    val finalY = (yCenter - textLayoutResult.size.height / 2)
      .coerceAtLeast(0f)
      .coerceAtMost(size.height - textLayoutResult.size.height)

    drawText(
      textMeasurer = textMeasurer,
      text = task.name,
      style = style,
      topLeft = Offset(finalX, finalY),
    )
  }
}

public fun DrawScope.drawXAxisLabels(
  textMeasurer: TextMeasurer,
  left: Float,
  bottom: Float,
  width: Float,
  maxMonth: Float,
  canvasHeight: Float,
  tasks: List<GanttTask>,
) {
  val style = TextStyle(fontSize = 10.sp, color = Color.Black)
  val safeMaxMonth = max(maxMonth, 1f)

  // If no tasks, skip
  if (tasks.isEmpty()) return

  // 1) Gather months from tasks (integer months for start..end).
  //    e.g., if start=2.5, duration=3.2 => range 2..5
  val distinctMonths = tasks
    .flatMap { task ->
      val start = task.startMonth.toInt()
      val end = (task.startMonth + task.duration).toInt()
      (start..end).toList() // all integer months in the range
    }
    .toSet()

  // 2) Also ensure we include 0 and the overall max
  val finalMonths = distinctMonths + 0 + safeMaxMonth.toInt()

  // 3) Sort them so labels appear left-to-right
  val sortedMonths = finalMonths.sorted()

  // 4) Draw a label for each distinct month
  sortedMonths.forEach { monthInt ->
    val fraction = monthInt / safeMaxMonth
    val x = left + fraction * width
    val label = monthInt.toString()

    val textLayoutResult = textMeasurer.measure(label, style)
    // Ensure the label isn't clipped off the bottom
    val safeBottom = min(bottom + 5f, canvasHeight - textLayoutResult.size.height)

    // Center horizontally at X
    val finalX = (x - textLayoutResult.size.width / 2)
      .coerceAtLeast(0f)
      .coerceAtMost(size.width - textLayoutResult.size.width)

    drawText(
      textMeasurer = textMeasurer,
      text = label,
      style = style,
      topLeft = Offset(finalX, safeBottom),
    )
  }
}
