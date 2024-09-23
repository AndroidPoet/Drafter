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
import kotlin.math.abs
import kotlin.math.absoluteValue

public class WaterfallChartRenderer : BarChartRenderer<WaterfallChartData> {
  override fun calculateMaxValue(data: WaterfallChartData): Float {
    val cumulativeValues = mutableListOf<Float>()
    var sum = data.initialValue
    cumulativeValues.add(sum)
    data.values.forEach { value ->
      sum += value
      cumulativeValues.add(sum)
    }
    val max = cumulativeValues.maxOrNull() ?: 0f
    val min = cumulativeValues.minOrNull() ?: 0f
    return maxOf(max.absoluteValue, min.absoluteValue)
  }

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
  ): Float = barWidth

  override fun drawBars(
    drawScope: DrawScope,
    data: WaterfallChartData,
    index: Int,
    left: Float,
    barWidth: Float,
    groupSpacing: Float,
    chartBottom: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  ) {
    val cumulativeValues = mutableListOf<Float>()
    var sum = data.initialValue
    cumulativeValues.add(sum)
    data.values.forEach { value ->
      sum += value
      cumulativeValues.add(sum)
    }

    // Starting and ending cumulative values for the bar
    val startValue = cumulativeValues[index]
    val endValue = cumulativeValues[index + 1]

    // Y positions
    val yStart = chartBottom - ((startValue / maxValue) * chartHeight)
    val yEnd = chartBottom - ((endValue / maxValue) * chartHeight)

    val top = minOf(yStart, yEnd)
    val height = abs(yEnd - yStart) * animationProgress

    val color = data.colors.getOrElse(index) { Color.Gray }

    drawScope.drawRect(
      color = color,
      topLeft = Offset(left, top),
      size = Size(barWidth, height),
    )

    // Optional: Draw lines connecting bars
    if (index > 0) {
      val previousEndValue = cumulativeValues[index]
      val previousYEnd = chartBottom - ((previousEndValue / maxValue) * chartHeight)
      drawScope.drawLine(
        color = Color.Black,
        start = Offset(left - barWidth - groupSpacing, previousYEnd),
        end = Offset(left, yStart),
        strokeWidth = 2f,
      )
    }
  }
}
