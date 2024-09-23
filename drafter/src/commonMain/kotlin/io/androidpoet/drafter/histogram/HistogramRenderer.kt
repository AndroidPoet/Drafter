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
package io.androidpoet.drafter.histogram

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.androidpoet.drafter.bars.BarChartRenderer
import kotlin.math.roundToInt

public class HistogramRenderer : BarChartRenderer<HistogramData> {
  override fun calculateMaxValue(data: HistogramData): Float {
    val maxFrequency = data.frequencies.maxOrNull() ?: 0f
    return if (maxFrequency > 0f) maxFrequency else 1f
  }

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

  override fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float = barWidth

  override fun drawBars(
    drawScope: DrawScope,
    data: HistogramData,
    index: Int,
    left: Float,
    barWidth: Float,
    groupSpacing: Float,
    chartBottom: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  ) {
    val frequency = data.frequencies[index]
    val safeMaxValue = if (maxValue > 0f) maxValue else 1f
    val barHeight = ((frequency / safeMaxValue) * chartHeight).coerceAtLeast(0f) * animationProgress
    val barColor = data.colors.getOrElse(index) { Color.Blue }

    drawScope.drawRect(
      color = barColor,
      topLeft = Offset(left, chartBottom - barHeight),
      size = Size(barWidth, barHeight),
    )
  }
}

public fun createHistogramData(
  dataPoints: List<Float>,
  binCount: Int,
  color: Color = Color.Blue,
): HistogramData {
  val minValue = dataPoints.minOrNull() ?: 0f
  val maxValue = dataPoints.maxOrNull() ?: minValue // Avoid maxValue < minValue
  val binSize = if (maxValue > minValue) (maxValue - minValue) / binCount else 1f

  val frequencies = MutableList(binCount) { 0f }
  val labels = MutableList(binCount) { "" }
  val colors = MutableList(binCount) { color }

  dataPoints.forEach { point ->
    val binIndex = ((point - minValue) / binSize).toInt().coerceIn(0, binCount - 1)
    frequencies[binIndex] += 1f
  }

  for (i in 0 until binCount) {
    val start = minValue + i * binSize
    val end = start + binSize
    labels[i] = "${formatToOneDecimal(start)}-${formatToOneDecimal(end)}"
  }

  return HistogramData(labels, frequencies, colors)
}

// Custom formatting function
internal fun formatToOneDecimal(value: Float): String = ((value * 10).roundToInt() / 10f).toString()
