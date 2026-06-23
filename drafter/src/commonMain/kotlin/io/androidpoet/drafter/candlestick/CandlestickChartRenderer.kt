package io.androidpoet.drafter.candlestick

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.candlestick.model.CandlestickData
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

    // Show every Nth x-label to avoid crowding.
    val labelEvery = max(1, count / 8)

    data.candles.forEachIndexed { index, candle ->
      val centerX = chartLeft + slot * index + slot / 2f
      val isUp = candle.close >= candle.open
      val color = if (isUp) DrafterColors.Green else DrafterColors.Coral

      fun yFor(value: Float): Float = chartBottom - (value - minLow) / range * chartHeight

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
  }

  private fun formatValue(value: Float): String {
    val rounded = (value * 10f).toInt() / 10f
    return if (rounded == rounded.toInt().toFloat()) rounded.toInt().toString() else rounded.toString()
  }
}
