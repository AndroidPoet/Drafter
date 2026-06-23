package io.androidpoet.drafter.bullet

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.bullet.model.BulletData
import io.androidpoet.drafter.theme.DrafterColors
import kotlin.math.max

@Immutable
public class BulletChartRenderer(
  public val data: BulletData,
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
    if (data.metrics.isEmpty()) return

    val labelColor = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight
    val bandBase = if (isSystemInDarkTheme) Color.White else Color.Black
    val labelStyle = TextStyle(fontSize = 11.sp, color = labelColor)

    val count = data.metrics.size
    val rowSlot = chartHeight / count
    val rowHeight = rowSlot * 0.55f

    // Left gutter for labels: ~30% of width.
    val gutter = chartWidth * 0.28f
    val trackLeft = chartLeft + gutter
    val trackWidth = chartWidth - gutter

    data.metrics.forEachIndexed { index, metric ->
      val rowTop = chartTop + rowSlot * index + (rowSlot - rowHeight) / 2f
      val rowCenterY = rowTop + rowHeight / 2f

      val sortedRanges = metric.ranges.sorted()
      val maxValue = max(
        max(sortedRanges.maxOrNull() ?: 0f, metric.value),
        metric.target,
      ).let { if (it <= 0f) 1f else it }

      // Qualitative range bands, increasingly darker translucent tint.
      sortedRanges.forEachIndexed { rIndex, rangeEnd ->
        val start = if (rIndex == 0) 0f else sortedRanges[rIndex - 1]
        val x0 = trackLeft + (start / maxValue) * trackWidth
        val x1 = trackLeft + (rangeEnd / maxValue) * trackWidth
        val alpha = 0.06f + 0.07f * rIndex
        drawScope.drawRoundRect(
          color = bandBase.copy(alpha = alpha),
          topLeft = Offset(x0, rowTop),
          size = Size(max(x1 - x0, 0f), rowHeight),
          cornerRadius = CornerRadius(4f, 4f),
        )
      }

      // Measure bar = value, thinner, rounded, animated width.
      val measureHeight = rowHeight * 0.42f
      val measureTop = rowCenterY - measureHeight / 2f
      val measureFullWidth = (metric.value / maxValue) * trackWidth
      val measureWidth = max(measureFullWidth * animationProgress, 0f)
      drawScope.drawRoundRect(
        color = metric.color.copy(alpha = 0.2f),
        topLeft = Offset(trackLeft, measureTop),
        size = Size(measureWidth, measureHeight),
        cornerRadius = CornerRadius(measureHeight / 2f, measureHeight / 2f),
      )
      drawScope.drawRoundRect(
        color = metric.color,
        topLeft = Offset(trackLeft, measureTop),
        size = Size(measureWidth, measureHeight),
        cornerRadius = CornerRadius(measureHeight / 2f, measureHeight / 2f),
      )

      // Vertical target tick.
      val targetX = trackLeft + (metric.target / maxValue) * trackWidth
      drawScope.drawLine(
        color = if (isSystemInDarkTheme) Color.White else Color.Black,
        start = Offset(targetX, rowTop - 2f),
        end = Offset(targetX, rowTop + rowHeight + 2f),
        strokeWidth = 3f,
      )

      // Label on the left of the row.
      val labelMeasured = textMeasurer.measure(metric.label, labelStyle)
      drawScope.drawText(
        textMeasurer = textMeasurer,
        text = metric.label,
        style = labelStyle,
        topLeft = Offset(chartLeft, rowCenterY - labelMeasured.size.height / 2f),
      )

      // Value at the end of the row.
      val valueText = formatValue(metric.value)
      val valueStyle = TextStyle(fontSize = 11.sp, color = metric.color)
      val valueMeasured = textMeasurer.measure(valueText, valueStyle)
      drawScope.drawText(
        textMeasurer = textMeasurer,
        text = valueText,
        style = valueStyle,
        topLeft = Offset(
          chartLeft + chartWidth - valueMeasured.size.width,
          rowTop - valueMeasured.size.height - 2f,
        ),
      )
    }
  }

  private fun formatValue(value: Float): String {
    val rounded = (value * 10f).toInt() / 10f
    return if (rounded == rounded.toInt().toFloat()) rounded.toInt().toString() else rounded.toString()
  }
}
