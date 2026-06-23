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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.androidpoet.drafter.bars.BarChartDataRenderer
import io.androidpoet.drafter.bars.model.SimpleBarChartData

/**
 * A renderer for simple bar charts where each category has a single bar.
 * This is the most basic form of a bar chart with one value per label.
 *
 * @property data The [SimpleBarChartData] containing labels, values, and colors for the bars
 */
@Immutable
public class BarChartRenderer(
  public val data: SimpleBarChartData,
) : BarChartDataRenderer {
  /**
   * Returns the list of labels for the X-axis.
   */
  override fun getLabels(): List<String> = data.labelsList

  /**
   * Returns 1 since simple bar charts have one bar per group.
   */
  override fun barsPerGroup(): Int = 1

  /**
   * Calculates the maximum value across all bars for scaling purposes.
   * Returns 0 if there are no values.
   */
  override fun calculateMaxValue(): Float = data.values.maxOrNull() ?: 0f

  /**
   * Calculates the width of bars and spacing between them based on chart dimensions.
   *
   * @param chartWidth Total width of the chart area
   * @param dataSize Number of data points (bars) to display
   * @param barsPerGroup Always 1 for simple bar charts (unused parameter)
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
   * For simple bar charts, group width equals bar width.
   */
  override fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float = barWidth

  /**
   * Draws a single bar at the specified position.
   *
   * @param drawScope Drawing context
   * @param index Index of the current bar
   * @param left Left position of the bar
   * @param barWidth Width of the bar
   * @param groupSpacing Spacing between bars (unused in simple charts)
   * @param chartBottom Y-coordinate of chart bottom
   * @param chartHeight Total height of chart
   * @param maxValue Maximum value for scaling
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
    val value = data.values[index]
    val barHeight = (value / maxValue) * chartHeight * animationProgress
    if (barHeight <= 0f) return
    val color = data.colors.getOrElse(index) { Color.Gray }

    // Slim the bar slightly for breathing room, round the top corners, and give
    // it a soft top-to-bottom gradient for a premium feel.
    val inset = barWidth * 0.16f
    val drawWidth = barWidth - inset * 2
    val topLeft = Offset(left + inset, chartBottom - barHeight)
    val corner = CornerRadius(drawWidth * 0.4f, drawWidth * 0.4f)

    drawScope.drawRoundRect(
      brush =
      Brush.verticalGradient(
        colors = listOf(color, color.copy(alpha = 0.72f)),
        startY = topLeft.y,
        endY = chartBottom,
      ),
      topLeft = topLeft,
      size = Size(drawWidth, barHeight),
      cornerRadius = corner,
    )
  }
}
