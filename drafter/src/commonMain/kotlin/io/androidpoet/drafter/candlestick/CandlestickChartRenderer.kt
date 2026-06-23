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
package io.androidpoet.drafter.candlestick

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.candlestick.model.CandlestickData
import io.androidpoet.drafter.candlestick.model.MovingAverage
import io.androidpoet.drafter.internal.smoothPath
import io.androidpoet.drafter.theme.DrafterColors
import kotlin.math.max

@Immutable
public class CandlestickChartRenderer(
  public val data: CandlestickData,
) : io.androidpoet.drafter.core.ChartRenderer {
  public fun draw(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  ) {
    if (data.candles.isEmpty()) return

    val minLow = data.candles.minOf { it.low }
    val maxHigh = data.candles.maxOf { it.high }
    val range = max(maxHigh - minLow, 0.0001f)

    val chartBottom = chartTop + chartHeight
    val gridColor = if (isSystemInDarkTheme) DrafterColors.GridDark else DrafterColors.GridLight
    val labelColor = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight

    // Axes.
    drawScope.drawLine(
      color = gridColor,
      start = Offset(chartLeft, chartTop),
      end = Offset(chartLeft, chartBottom),
      strokeWidth = 1.5f,
    )
    drawScope.drawLine(
      color = gridColor,
      start = Offset(chartLeft, chartBottom),
      end = Offset(chartLeft + chartWidth, chartBottom),
      strokeWidth = 1.5f,
    )

    val labelStyle = TextStyle(fontSize = 10.sp, color = labelColor)

    // Y axis labels from min(low)..max(high).
    val ySteps = 4
    for (i in 0..ySteps) {
      val value = minLow + range * (i.toFloat() / ySteps)
      val y = chartBottom - (value - minLow) / range * chartHeight
      // Subtle gridline.
      drawScope.drawLine(
        color = gridColor,
        start = Offset(chartLeft, y),
        end = Offset(chartLeft + chartWidth, y),
        strokeWidth = 1f,
      )
      val label = formatValue(value)
      val measured = textMeasurer.measure(label, labelStyle)
      drawScope.drawText(
        textMeasurer = textMeasurer,
        text = label,
        style = labelStyle,
        topLeft = Offset(chartLeft - measured.size.width - 6f, y - measured.size.height / 2f),
      )
    }

    val count = data.candles.size
    val slot = chartWidth / count
    val bodyWidth = (slot * 0.6f).coerceAtLeast(2f)

    fun yFor(value: Float): Float = chartBottom - (value - minLow) / range * chartHeight
    fun centerXFor(index: Int): Float = chartLeft + slot * index + slot / 2f

    // Show every Nth x-label to avoid crowding.
    val labelEvery = max(1, count / 8)

    data.candles.forEachIndexed { index, candle ->
      val centerX = centerXFor(index)
      val isUp = candle.close >= candle.open
      val color = if (isUp) DrafterColors.Green else DrafterColors.Coral

      // Wick (low -> high), animated grow from the body center.
      val bodyTopValue = max(candle.open, candle.close)
      val bodyBottomValue = kotlin.math.min(candle.open, candle.close)
      val bodyCenterValue = (bodyTopValue + bodyBottomValue) / 2f
      val centerY = yFor(bodyCenterValue)

      val highY = yFor(candle.high)
      val lowY = yFor(candle.low)
      val animHighY = centerY + (highY - centerY) * animationProgress
      val animLowY = centerY + (lowY - centerY) * animationProgress

      drawScope.drawLine(
        color = color.copy(alpha = 0.9f),
        start = Offset(centerX, animHighY),
        end = Offset(centerX, animLowY),
        strokeWidth = 2f,
      )

      // Body rect spanning open<->close, animated growing from center.
      val fullTopY = yFor(bodyTopValue)
      val fullBottomY = yFor(bodyBottomValue)
      val fullBodyHeight = max(fullBottomY - fullTopY, 2f)
      val animBodyHeight = fullBodyHeight * animationProgress
      val bodyTopY = centerY - animBodyHeight / 2f

      val corner = CornerRadius(3f, 3f)
      // Soft translucent halo behind the body for a premium feel.
      drawScope.drawRoundRect(
        color = color.copy(alpha = 0.18f * animationProgress),
        topLeft = Offset(centerX - bodyWidth / 2f - 2f, bodyTopY - 2f),
        size = Size(bodyWidth + 4f, animBodyHeight + 4f),
        cornerRadius = corner,
      )
      drawScope.drawRoundRect(
        color = color.copy(alpha = animationProgress),
        topLeft = Offset(centerX - bodyWidth / 2f, bodyTopY),
        size = Size(bodyWidth, animBodyHeight),
        cornerRadius = corner,
      )

      if (index % labelEvery == 0) {
        val measured = textMeasurer.measure(candle.label, labelStyle)
        drawScope.drawText(
          textMeasurer = textMeasurer,
          text = candle.label,
          style = labelStyle,
          topLeft = Offset(centerX - measured.size.width / 2f, chartBottom + 6f),
        )
      }
    }

    // Moving-average overlays (MA5 / MA10 / MA20 ...) on top of the candles —
    // the classic K-line study. Each line is revealed left-to-right with the
    // same entrance animation as the candles.
    val reveal = chartLeft + chartWidth * animationProgress.coerceIn(0f, 1f)
    data.movingAverages.forEach { ma ->
      val points = movingAveragePoints(ma, { centerXFor(it) }, { yFor(it) })
      if (points.size < 2) return@forEach
      drawScope.clipRect(right = reveal) {
        drawPath(
          path = smoothPath(points),
          color = ma.color,
          style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
      }
    }

    // Compact legend (MAn in each line's color), top-left inside the plot.
    var legendX = chartLeft + 4f
    data.movingAverages.forEach { ma ->
      val legendStyle = TextStyle(fontSize = 10.sp, color = ma.color)
      val text = "MA${ma.period}"
      val measured = textMeasurer.measure(text, legendStyle)
      drawScope.drawText(
        textMeasurer = textMeasurer,
        text = text,
        style = legendStyle,
        topLeft = Offset(legendX, chartTop + 2f),
      )
      legendX += measured.size.width + 10f
    }
  }

  /** Builds the smoothed point list for a single moving-average line. */
  private inline fun movingAveragePoints(
    ma: MovingAverage,
    centerXFor: (Int) -> Float,
    yFor: (Float) -> Float,
  ): List<Offset> {
    val count = data.candles.size
    if (ma.period <= 0 || ma.period > count) return emptyList()
    val points = ArrayList<Offset>(count)
    for (i in ma.period - 1 until count) {
      var sum = 0f
      for (j in (i - ma.period + 1)..i) sum += data.candles[j].close
      points.add(Offset(centerXFor(i), yFor(sum / ma.period)))
    }
    return points
  }

  private fun formatValue(value: Float): String = io.androidpoet.drafter.core.formatChartValue(
    value,
    decimals = 1,
  )
}
