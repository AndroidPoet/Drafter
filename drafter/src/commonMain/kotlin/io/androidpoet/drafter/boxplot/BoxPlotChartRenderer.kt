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
package io.androidpoet.drafter.boxplot

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.boxplot.model.BoxPlotData
import io.androidpoet.drafter.theme.DrafterColors

public interface BoxPlotRenderer : io.androidpoet.drafter.core.ChartRenderer {
  public fun draw(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  )
}

@Immutable
public class BoxPlotChartRenderer(
  public val data: BoxPlotData,
) : BoxPlotRenderer {
  override fun draw(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  ) {
    val groups = data.groups
    if (groups.isEmpty()) return

    val chartBottom = chartTop + chartHeight
    val gridColor = if (isSystemInDarkTheme) DrafterColors.GridDark else DrafterColors.GridLight
    val labelColor = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight

    // Global value range across all groups.
    val globalMin = groups.minOf { it.min }
    val globalMax = groups.maxOf { it.max }
    val range = (globalMax - globalMin).takeIf { it > 0f } ?: 1f

    fun valueToY(value: Float): Float =
      chartBottom - ((value - globalMin) / range) * chartHeight

    // Gridlines + y labels.
    val gridLines = 5
    val labelStyle =
      TextStyle(
        fontSize = 10.sp,
        color = labelColor,
      )
    for (i in 0..gridLines) {
      val fraction = i.toFloat() / gridLines
      val value = globalMin + fraction * range
      val y = valueToY(value)
      drawScope.drawLine(
        color = gridColor,
        start = Offset(chartLeft, y),
        end = Offset(chartLeft + chartWidth, y),
        strokeWidth = 1f,
      )
      val label = formatValue(value)
      val measured = textMeasurer.measure(label, labelStyle)
      drawScope.drawText(
        textMeasurer = textMeasurer,
        text = label,
        style = labelStyle,
        topLeft =
        Offset(
          chartLeft - measured.size.width - 6f,
          y - measured.size.height / 2f,
        ),
      )
    }

    // Distribute group columns evenly across the width.
    val columnWidth = chartWidth / groups.size
    val boxWidth = (columnWidth * 0.45f).coerceAtMost(70f)

    groups.forEach { group ->
      val index = groups.indexOf(group)
      val centerX = chartLeft + columnWidth * (index + 0.5f)

      val yMin = valueToY(group.min)
      val yQ1 = valueToY(group.q1)
      val yMedian = valueToY(group.median)
      val yQ3 = valueToY(group.q3)
      val yMax = valueToY(group.max)

      val progress = animationProgress

      // Whiskers extend out from the median line.
      val whiskerTopY = yMedian + (yMax - yMedian) * progress
      val whiskerBottomY = yMedian + (yMin - yMedian) * progress
      drawScope.drawLine(
        color = group.color,
        start = Offset(centerX, whiskerTopY),
        end = Offset(centerX, whiskerBottomY),
        strokeWidth = 2f,
      )
      // Caps at min and max.
      val capHalf = boxWidth * 0.3f
      drawScope.drawLine(
        color = group.color,
        start = Offset(centerX - capHalf, whiskerTopY),
        end = Offset(centerX + capHalf, whiskerTopY),
        strokeWidth = 2f,
      )
      drawScope.drawLine(
        color = group.color,
        start = Offset(centerX - capHalf, whiskerBottomY),
        end = Offset(centerX + capHalf, whiskerBottomY),
        strokeWidth = 2f,
      )

      // Box grows vertically out from the median line.
      val boxTopY = yMedian + (yQ3 - yMedian) * progress
      val boxBottomY = yMedian + (yQ1 - yMedian) * progress
      val boxLeft = centerX - boxWidth / 2f
      val boxTop = minOf(boxTopY, boxBottomY)
      val boxHeight = kotlin.math.abs(boxBottomY - boxTopY)
      val corner = CornerRadius(8f, 8f)

      drawScope.drawRoundRect(
        color = group.color.copy(alpha = 0.35f),
        topLeft = Offset(boxLeft, boxTop),
        size = Size(boxWidth, boxHeight),
        cornerRadius = corner,
      )
      drawScope.drawRoundRect(
        color = group.color,
        topLeft = Offset(boxLeft, boxTop),
        size = Size(boxWidth, boxHeight),
        cornerRadius = corner,
        style = Stroke(width = 2f),
      )

      // Bold median line across the box (always at the median position).
      drawScope.drawLine(
        color = group.color,
        start = Offset(boxLeft, yMedian),
        end = Offset(boxLeft + boxWidth, yMedian),
        strokeWidth = 3.5f,
      )

      // X label under each box.
      val measured = textMeasurer.measure(group.label, labelStyle)
      drawScope.drawText(
        textMeasurer = textMeasurer,
        text = group.label,
        style = labelStyle,
        topLeft =
        Offset(
          centerX - measured.size.width / 2f,
          chartBottom + 6f,
        ),
      )
    }
  }

  private fun formatValue(value: Float): String = io.androidpoet.drafter.core.formatChartValue(
    value,
    decimals = 1,
  )
}
