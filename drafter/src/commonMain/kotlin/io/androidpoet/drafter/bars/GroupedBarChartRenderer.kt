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
package io.androidpoet.drafter.bars

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

public class GroupedBarChartRenderer : BarChartRenderer<GroupedBarChartData> {
  override fun calculateMaxValue(data: GroupedBarChartData): Float =
    data.groupedValues.flatten().maxOrNull() ?: 0f

  override fun calculateBarAndSpacing(
    chartWidth: Float,
    dataSize: Int,
    barsPerGroup: Int,
  ): Pair<Float, Float> {
    val totalGroupSpacing = chartWidth * 0.1f
    val groupSpacing = totalGroupSpacing / (dataSize + 1)
    val availableWidth = chartWidth - totalGroupSpacing
    val totalBarSpacingPerGroup = (barsPerGroup - 1) * 4f
    val barWidth = (availableWidth / dataSize - totalBarSpacingPerGroup) / barsPerGroup
    return Pair(barWidth, groupSpacing)
  }

  override fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float {
    val totalBarSpacingPerGroup = (barsPerGroup - 1) * 4f
    return barWidth * barsPerGroup + totalBarSpacingPerGroup
  }

  override fun drawBars(
    drawScope: DrawScope,
    data: GroupedBarChartData,
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
    val barsPerGroup = groupValues.size
    val barSpacing = 4f
    var currentLeft = left

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
