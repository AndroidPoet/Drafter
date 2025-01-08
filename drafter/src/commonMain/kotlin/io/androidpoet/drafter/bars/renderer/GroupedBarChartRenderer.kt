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
import io.androidpoet.drafter.bars.model.GroupedBarChartData

/**
 * Renderer for grouped bar charts where multiple bars are displayed side by side for each label.
 * This renderer handles the layout and drawing of grouped bars with proper spacing and sizing.
 *
 * @property data The [GroupedBarChartData] containing the data to be rendered
 */
public class GroupedBarChartRenderer(
  public val data: GroupedBarChartData,
) : BarChartDataRenderer {
  /**
   * Returns the list of labels for the X-axis.
   */
  override fun getLabels(): List<String> = data.labelsList

  /**
   * Returns the number of bars in each group.
   */
  override fun barsPerGroup(): Int = data.itemNames.size

  /**
   * Calculates the maximum value across all bars for scaling purposes.
   */
  override fun calculateMaxValue(): Float = data.groupedValues.flatten().maxOrNull() ?: 0f

  /**
   * Calculates the width of individual bars and spacing between groups based on the chart dimensions.
   *
   * @param chartWidth Total width of the chart
   * @param dataSize Number of groups in the data
   * @param barsPerGroup Number of bars in each group
   * @return Pair of (barWidth, groupSpacing)
   */
  override fun calculateBarAndSpacing(
    chartWidth: Float,
    dataSize: Int,
    barsPerGroup: Int,
  ): Pair<Float, Float> {
    // Reserve 10% of chart width for spacing between groups
    val totalGroupSpacing = chartWidth * 0.1f
    val groupSpacing = totalGroupSpacing / (dataSize + 1)

    // Calculate bar width based on available space
    val availableWidth = chartWidth - totalGroupSpacing
    val totalBarSpacingPerGroup =
      (barsPerGroup - 1) * 4f // 4f is the spacing between bars in a group
    val barWidth = (availableWidth / dataSize - totalBarSpacingPerGroup) / barsPerGroup

    return Pair(barWidth, groupSpacing)
  }

  /**
   * Calculates the total width of a group of bars including spacing.
   *
   * @param barWidth Width of individual bars
   * @param barsPerGroup Number of bars in each group
   * @return Total width of the group
   */
  override fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float {
    val totalBarSpacingPerGroup = (barsPerGroup - 1) * 4f
    return (barWidth * barsPerGroup) + totalBarSpacingPerGroup
  }

  /**
   * Draws the bars for a specific group in the chart.
   *
   * @param drawScope The DrawScope to draw on
   * @param index Index of the group being drawn
   * @param left Left position of the group
   * @param barWidth Width of individual bars
   * @param groupSpacing Spacing between groups
   * @param chartBottom Y-coordinate of the chart bottom
   * @param chartHeight Total height of the chart
   * @param maxValue Maximum value in the dataset for scaling
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
    val groupValues = data.groupedValues[index]
    val barSpacing = 4f // Spacing between bars within a group
    var currentLeft = left

    // Draw each bar in the group
    groupValues.forEachIndexed { barIndex, value ->
      val barHeight = (value / maxValue) * chartHeight * animationProgress
      val barColor = data.colors.getOrElse(barIndex) { Color.Gray }

      drawScope.drawRect(
        color = barColor,
        topLeft = Offset(currentLeft, chartBottom - barHeight),
        size = Size(barWidth, barHeight),
      )

      currentLeft += barWidth + barSpacing
    }
  }
}
