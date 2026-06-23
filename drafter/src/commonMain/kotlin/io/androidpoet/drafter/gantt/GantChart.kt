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
package io.androidpoet.drafter.gantt

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
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
  animate: Boolean = true,
  isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
) {
  val textMeasurer = rememberTextMeasurer()
  val animationProgress = remember { Animatable(0f) }
  LaunchedEffect(animate) {
    animationProgress.animateTo(
      targetValue = 1f,
      animationSpec =
      tween(
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
    val (maxMonth, _) = renderer.calculateMaxValues()
    drawAxes(
      left = chartLeft,
      top = chartTop,
      bottom = chartBottom,
      width = chartWidth,
      isSystemInDarkTheme = isSystemInDarkTheme,
    )
    drawYAxisLabels(
      textMeasurer = textMeasurer,
      left = chartLeft,
      top = chartTop,
      bottom = chartBottom,
      tasks = renderer.data.tasks,
      isSystemInDarkTheme = isSystemInDarkTheme,
    )
    drawXAxisLabels(
      textMeasurer = textMeasurer,
      left = chartLeft,
      bottom = chartBottom,
      width = chartWidth,
      maxMonth = maxMonth,
      canvasHeight = size.height,
      tasks = renderer.data.tasks,
      isSystemInDarkTheme = isSystemInDarkTheme,
    )
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
  isSystemInDarkTheme: Boolean,
) {
  drawLine(
    if (isSystemInDarkTheme) Color.White else Color.Black,
    Offset(left, top),
    Offset(left, bottom),
    strokeWidth = 2f,
  )
  drawLine(
    if (isSystemInDarkTheme) Color.White else Color.Black,
    Offset(left, bottom),
    Offset(left + width, bottom),
    strokeWidth = 2f,
  )
}

public fun DrawScope.drawYAxisLabels(
  textMeasurer: TextMeasurer,
  left: Float,
  top: Float,
  bottom: Float,
  tasks: List<GanttTask>,
  isSystemInDarkTheme: Boolean,
) {
  if (tasks.isEmpty()) return

  val style =
    TextStyle(fontSize = 10.sp, color = if (isSystemInDarkTheme) Color.White else Color.Black)
  val taskHeight = max((bottom - top) / tasks.size, 1f)

  tasks.forEachIndexed { index, task ->
    val yCenter = top + index * taskHeight + taskHeight / 2
    val textLayoutResult = textMeasurer.measure(task.name, style)
    val finalX = (left - textLayoutResult.size.width - 5f).coerceAtLeast(0f)

    val finalY =
      (yCenter - textLayoutResult.size.height / 2)
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
  isSystemInDarkTheme: Boolean,
) {
  val style =
    TextStyle(fontSize = 10.sp, color = if (isSystemInDarkTheme) Color.White else Color.Black)
  val safeMaxMonth = max(maxMonth, 1f)
  if (tasks.isEmpty()) return
  val distinctMonths =
    tasks
      .flatMap { task ->
        val start = task.startMonth.toInt()
        val end = (task.startMonth + task.duration).toInt()
        (start..end).toList() // all integer months in the range
      }.toSet()
  val finalMonths = distinctMonths + 0 + safeMaxMonth.toInt()
  val sortedMonths = finalMonths.sorted()
  sortedMonths.forEach { monthInt ->
    val fraction = monthInt / safeMaxMonth
    val x = left + fraction * width
    val label = monthInt.toString()

    val textLayoutResult = textMeasurer.measure(label, style)
    val safeBottom = min(bottom + 5f, canvasHeight - textLayoutResult.size.height)
    val finalX =
      (x - textLayoutResult.size.width / 2)
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
