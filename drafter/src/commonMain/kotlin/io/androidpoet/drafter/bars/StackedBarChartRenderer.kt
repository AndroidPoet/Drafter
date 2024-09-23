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
import androidx.compose.ui.graphics.drawscope.DrawScope

public class StackedBarChartRenderer : BarChartRenderer<StackedBarChartData> {
  override fun calculateMaxValue(data: StackedBarChartData): Float = data.stacks.maxOf { it.sum() }

  override fun calculateBarAndSpacing(
    chartWidth: Float,
    dataSize: Int,
    barsPerGroup: Int,
  ): Pair<Float, Float> {
    val totalSpacing = chartWidth * 0.1f // 10% spacing
    val groupSpacing = totalSpacing / (dataSize + 1)
    val availableWidth = chartWidth - totalSpacing
    val barWidth = availableWidth / dataSize
    return Pair(barWidth, groupSpacing)
  }

  override fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float {
    return barWidth // Only one bar per group
  }

  override fun drawBars(
    drawScope: DrawScope,
    data: StackedBarChartData,
    index: Int,
    left: Float,
    barWidth: Float,
    groupSpacing: Float,
    chartBottom: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  ) {
    var bottom = chartBottom
    data.stacks[index].forEachIndexed { stackIndex, value ->
      val barHeight = (value / maxValue) * chartHeight * animationProgress
      drawScope.drawRect(
        color = data.colors[stackIndex],
        topLeft = Offset(left, bottom - barHeight),
        size = Size(barWidth, barHeight),
      )
      bottom -= barHeight
    }
  }
}