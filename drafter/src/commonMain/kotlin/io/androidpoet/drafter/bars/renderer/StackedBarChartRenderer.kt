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
package io.androidpoet.drafter.bars.renderer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.androidpoet.drafter.bars.BarChartDataRenderer
import io.androidpoet.drafter.bars.model.StackedBarChartData
import kotlin.math.roundToInt

/**
 * A renderer for stacked bar charts where multiple values are stacked vertically in a single bar.
 * The bars are distributed evenly across the available width to ensure full X-axis coverage.
 *
 * @property data The [StackedBarChartData] containing labels, stacked values, and colors for the segments
 */
public class StackedBarChartRenderer(
  public val data: StackedBarChartData,
) : BarChartDataRenderer {
  override fun getLabels(): List<String> =
    data.labelsList.map { label ->
      label.toFloatOrNull()?.let { formatToOneDecimal(it) } ?: label
    }

  override fun barsPerGroup(): Int = 1

  override fun calculateMaxValue(): Float = data.stacks.maxOf { it.sum() }

  /**
   * Calculates the width of bars and spacing between them to fill the entire chart width.
   * Ensures consistent spacing and bar widths across the full X-axis.
   *
   * @param chartWidth Total width of the chart area
   * @param dataSize Number of stacked bars to display
   * @param barsPerGroup Always 1 for stacked charts (unused parameter)
   * @return Pair of (barWidth, spacing)
   */
  override fun calculateBarAndSpacing(
    chartWidth: Float,
    dataSize: Int,
    barsPerGroup: Int,
  ): Pair<Float, Float> {
    if (dataSize <= 0) return Pair(0f, 0f)
    val totalGapSpace = chartWidth * 0.2f // Increased from 0.1f to give more spacing
    val groupSpacing = totalGapSpace / (dataSize + 1)
    val availableWidth = chartWidth - totalGapSpace
    val barWidth = (availableWidth / dataSize).coerceAtLeast(0f)

    return Pair(barWidth, groupSpacing)
  }

  override fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float = barWidth

  /**
   * Draws a stacked bar with multiple segments, ensuring proper vertical stacking
   * and consistent width across the chart.
   */
  override fun drawBars(
    drawScope: DrawScope,
    index: Int,
    left: Float,
    barWidth: Float,
    groupSpacing: Float,
    chartBottom: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  ) {
    if (index >= data.stacks.size) return
    var currentBottom = chartBottom
    val stackValues = data.stacks[index]
    stackValues.forEachIndexed { stackIndex, value ->
      val safeMaxValue = maxValue.coerceAtLeast(1e-6f) // Prevent division by zero
      val barHeight = (value / safeMaxValue) * chartHeight * animationProgress
      val barColor = data.colors.getOrElse(stackIndex) { Color.Gray }
      drawScope.drawRect(
        color = barColor,
        topLeft = Offset(left, currentBottom - barHeight),
        size = Size(barWidth, barHeight),
      )
      currentBottom -= barHeight
    }
  }
}

/**
 * Formats a float value to exactly one decimal place using platform-independent approach.
 *
 * @param value Float value to format
 * @return String representation with exactly one decimal place
 */
private fun formatToOneDecimal(value: Float): String = ((value * 10).roundToInt() / 10f).toString()
