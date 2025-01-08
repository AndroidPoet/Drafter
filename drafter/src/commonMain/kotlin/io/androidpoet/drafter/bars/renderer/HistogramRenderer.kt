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
import io.androidpoet.drafter.bars.model.HistogramData
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * A specialized renderer for histogram charts that automatically bins data points into frequency distributions.
 * This renderer handles the binning process and renders the resulting histogram as a bar chart.
 *
 * @property dataPoints List of raw data points to be binned
 * @property binCount Number of bins to divide the data into
 * @property color Default color for the histogram bars (defaults to Blue)
 */
public class HistogramRenderer(
  dataPoints: List<Float>,
  binCount: Int,
  color: Color = Color.Blue,
) : BarChartDataRenderer {
  /**
   * Computed histogram data containing the binned frequencies and labels.
   * This is publicly accessible for reference (e.g., for Y-axis scaling).
   */
  private val histogramData: HistogramData

  init {
    // Process raw data points into binned histogram data
    histogramData = createHistogramData(dataPoints, binCount, color)
  }

  override fun getLabels(): List<String> = histogramData.labels

  override fun barsPerGroup(): Int = 1

  /**
   * Calculates the maximum frequency across all bins.
   * Returns 1f if no data is present to avoid division by zero.
   */
  override fun calculateMaxValue(): Float {
    val maxFreq = histogramData.frequencies.maxOrNull() ?: 0f
    return if (maxFreq > 0f) maxFreq else 1f
  }

  /**
   * Calculates the width of bars and spacing between them based on chart dimensions.
   * Includes safety checks to prevent negative values.
   *
   * @param chartWidth Total width of the chart area
   * @param dataSize Number of bins in the histogram
   * @param barsPerGroup Always 1 for histograms (unused parameter)
   * @return Pair of (barWidth, spacing) with non-negative values
   */
  override fun calculateBarAndSpacing(
    chartWidth: Float,
    dataSize: Int,
    barsPerGroup: Int,
  ): Pair<Float, Float> {
    // Safety check for invalid dimensions
    if (dataSize <= 0 || chartWidth <= 0f) {
      return 0f to 0f
    }

    // Reserve 10% of chart width for spacing between bars
    val totalSpacing = chartWidth * 0.1f
    val rawGroupSpacing = totalSpacing / (dataSize + 1)
    val availableWidth = chartWidth - totalSpacing
    val rawBarWidth = availableWidth / dataSize

    // Ensure we never return negative values
    val safeBarWidth = rawBarWidth.coerceAtLeast(0f)
    val safeGroupSpacing = rawGroupSpacing.coerceAtLeast(0f)

    return safeBarWidth to safeGroupSpacing
  }

  /**
   * Returns the width of a single bar as the group width.
   * For histograms, group width equals bar width since there's one bar per group.
   */
  override fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float = barWidth

  /**
   * Draws a single histogram bar at the specified position.
   *
   * @param drawScope Drawing context
   * @param index Index of the current bin
   * @param left Left position of the bar
   * @param barWidth Width of the bar
   * @param groupSpacing Spacing between bars
   * @param chartBottom Y-coordinate of chart bottom
   * @param chartHeight Total height of chart
   * @param maxValue Maximum frequency value for scaling
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
    val freq = histogramData.frequencies[index]
    val safeMax = max(maxValue, 1f) // Prevent division by zero

    // Calculate bar height with animation
    val barHeight = (freq / safeMax) * chartHeight * animationProgress

    val barColor = histogramData.colors.getOrElse(index) { Color.Blue }

    // Draw the histogram bar
    drawScope.drawRect(
      color = barColor,
      topLeft = Offset(left, chartBottom - barHeight),
      size = Size(barWidth, barHeight),
    )
  }

  /**
   * Creates histogram data by binning raw data points into frequency distributions.
   *
   * @param dataPoints Raw data points to be binned
   * @param binCount Number of bins to create
   * @param color Color for the histogram bars
   * @return [HistogramData] containing bin labels, frequencies, and colors
   */
  private fun createHistogramData(
    dataPoints: List<Float>,
    binCount: Int,
    color: Color,
  ): HistogramData {
    // Find data range
    val minVal = dataPoints.minOrNull() ?: 0f
    val maxVal = dataPoints.maxOrNull() ?: minVal
    val binSize = if (maxVal > minVal) (maxVal - minVal) / binCount else 1f

    // Initialize arrays for bin data
    val frequencies = MutableList(binCount) { 0f }
    val labels = MutableList(binCount) { "" }
    val colors = MutableList(binCount) { color }

    // Count frequencies for each bin
    dataPoints.forEach { point ->
      val binIndex = ((point - minVal) / binSize).toInt().coerceIn(0, binCount - 1)
      frequencies[binIndex] += 1f
    }

    // Generate bin range labels (e.g., "0.0-1.0")
    for (i in 0 until binCount) {
      val start = minVal + i * binSize
      val end = start + binSize
      labels[i] = "${formatToOneDecimal(start)}-${formatToOneDecimal(end)}"
    }

    return HistogramData(labels, frequencies, colors)
  }

  /**
   * Formats a float value to one decimal place.
   *
   * @param value Float value to format
   * @return String representation with one decimal place
   */
  private fun formatToOneDecimal(value: Float): String =
    ((value * 10).roundToInt() / 10f).toString()
}
