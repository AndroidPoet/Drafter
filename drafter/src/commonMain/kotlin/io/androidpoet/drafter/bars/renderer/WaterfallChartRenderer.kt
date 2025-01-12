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
import io.androidpoet.drafter.bars.model.WaterfallChartData
import kotlin.math.abs
import kotlin.math.absoluteValue

/**
 * A renderer for waterfall charts that visualize incremental changes from an initial value.
 * Each bar represents a change, with:
 * - Positive changes displayed as upward bars
 * - Negative changes displayed as downward bars
 * - Optional connector lines showing the flow between changes
 *
 * @property data The [WaterfallChartData] containing initial value, changes, and colors
 */
public class WaterfallChartRenderer(
  public val data: WaterfallChartData,
) : BarChartDataRenderer {
  override fun getLabels(): List<String> = data.labelsList

  /**
   * Returns 1 since waterfall charts have one bar per change.
   */
  override fun barsPerGroup(): Int = 1

  /**
   * Calculates the maximum absolute value needed for scaling the chart.
   * Considers both positive and negative cumulative values to ensure proper scaling.
   */
  override fun calculateMaxValue(): Float {
    val cumulativeValues = mutableListOf<Float>()
    var sum = data.initialValue
    cumulativeValues.add(sum)

    data.values.forEach { value ->
      sum += value
      cumulativeValues.add(sum)
    }
    val max = cumulativeValues.maxOrNull()?.absoluteValue ?: 0f
    val min = cumulativeValues.minOrNull()?.absoluteValue ?: 0f

    return maxOf(max, min)
  }

  /**
   * Calculates the width of bars and spacing between them based on chart dimensions.
   *
   * @param chartWidth Total width of the chart area
   * @param dataSize Number of changes to display
   * @param barsPerGroup Always 1 for waterfall charts (unused parameter)
   * @return Pair of (barWidth, spacing)
   */
  override fun calculateBarAndSpacing(
    chartWidth: Float,
    dataSize: Int,
    barsPerGroup: Int,
  ): Pair<Float, Float> {
    val totalSpacing = chartWidth * 0.1f
    val groupSpacing = totalSpacing / (dataSize + 1)
    val availableWidth = chartWidth - totalSpacing
    val barWidth = availableWidth / dataSize

    return Pair(barWidth, groupSpacing)
  }

  /**
   * Returns the width of a single bar as the group width.
   * For waterfall charts, group width equals bar width.
   */
  override fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float = barWidth

  /**
   * Draws a bar representing a change and optionally connects it to the previous value.
   *
   * @param drawScope Drawing context
   * @param index Index of the current change
   * @param left Left position of the bar
   * @param barWidth Width of the bar
   * @param groupSpacing Spacing between bars
   * @param chartBottom Y-coordinate of chart bottom
   * @param chartHeight Total height of chart
   * @param maxValue Maximum absolute value for scaling
   * @param animationProgress Current animation progress (0-1)
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
    if (index >= data.values.size) {
      return
    }
    val cumulativeValues = mutableListOf<Float>()
    var sum = data.initialValue
    cumulativeValues.add(sum)
    data.values.forEach { value ->
      sum += value
      cumulativeValues.add(sum)
    }
    val startValue = cumulativeValues[index]
    val endValue = cumulativeValues[index + 1]
    val yStart = chartBottom - ((startValue / maxValue) * chartHeight)
    val yEnd = chartBottom - ((endValue / maxValue) * chartHeight)
    val top = minOf(yStart, yEnd)
    val height = abs(yEnd - yStart) * animationProgress
    val barColor = data.colors.getOrElse(index) { Color.Gray }
    drawScope.drawRect(
      color = barColor,
      topLeft = Offset(left, top),
      size = Size(barWidth, height),
    )
    if (index > 0) {
      val prevYEnd = cumulativeValues[index]
      val previousYCoord = chartBottom - ((prevYEnd / maxValue) * chartHeight)

      drawScope.drawLine(
        color = Color.Black,
        start = Offset(left - barWidth - groupSpacing, previousYCoord),
        end = Offset(left, yStart),
        strokeWidth = 2f,
      )
    }
  }
}
