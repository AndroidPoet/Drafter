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
package io.androidpoet.drafter.buble

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

public class SimpleBubbleChartDataRenderer(
  private val data: BubbleChartData,
) : BubbleChartDataRenderer {
  override fun getLabels(): List<String> = emptyList()

  override fun getValueRanges(): ValueRanges {
    val allBubbles = data.series.flatten()
    val xMin = 0f // Always start from 0
    val yMin = 0f // Always start from 0
    val xMax = roundToNiceNumber(allBubbles.maxOfOrNull { it.x } ?: 0f)
    val yMax = roundToNiceNumber(allBubbles.maxOfOrNull { it.y } ?: 0f)
    return ValueRanges(xMin, xMax, yMin, yMax)
  }

  override fun getMaxValues(): Pair<Float, Float> {
    val ranges = getValueRanges()
    return Pair(ranges.xMax, ranges.yMax)
  }

  private fun roundToNiceNumber(value: Float): Float =
    when {
      value <= 50f -> (((value + 9) / 10).toInt() * 10).toFloat()
      value <= 100f -> (((value + 24) / 25).toInt() * 25).toFloat()
      else -> (((value + 49) / 50).toInt() * 50).toFloat()
    }

  override fun drawBubbles(
    drawScope: DrawScope,
    chartWidth: Float,
    chartHeight: Float,
    originX: Float,
    originY: Float,
    animationProgress: Float,
  ) {
    val ranges = getValueRanges()
    val maxBubbleSize = data.series.flatten().maxOfOrNull { it.size } ?: 0f

    data.series.forEachIndexed { seriesIndex, series ->
      series.forEachIndexed { bubbleIndex, bubble ->
        val delay = (seriesIndex * series.size + bubbleIndex) * 0.1f
        val bubbleProgress = (animationProgress - delay).coerceIn(0f, 1f)
        val x = originX + (bubble.x / ranges.xMax) * chartWidth
        val y = originY - (bubble.y / ranges.yMax) * chartHeight
        val scaleFactor = minOf(chartWidth, chartHeight) / 6f
        val scaledSize = (bubble.size / maxBubbleSize) * scaleFactor

        drawScope.drawCircle(
          color = bubble.color,
          radius = scaledSize * bubbleProgress,
          center = Offset(x, y),
        )
      }
    }
  }
}

public data class ValueRanges(
  val xMin: Float,
  val xMax: Float,
  val yMin: Float,
  val yMax: Float,
)
