package io.androidpoet.drafter.funnel

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.funnel.model.FunnelData

@Immutable
public class FunnelChartRenderer(
  public val data: FunnelData,
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
    val stages = data.stages
    if (stages.isEmpty()) return

    val maxValue = stages.maxOf { it.value }.takeIf { it > 0f } ?: 1f
    val centerX = chartLeft + chartWidth / 2f
    val gap = chartHeight * 0.02f
    val count = stages.size
    val bandHeight = (chartHeight - gap * (count - 1)) / count
    // The narrowest band gets a sensible minimum width so the funnel never pinches to nothing.
    val minWidthFraction = 0.12f

    fun widthFor(value: Float): Float {
      val fraction = minWidthFraction + (1f - minWidthFraction) * (value / maxValue)
      return chartWidth * fraction
    }

    val labelStyle =
      TextStyle(
        fontSize = 13.sp,
        color = if (isSystemInDarkTheme) Color.White else Color(0xFF1B1E25),
      )
    val valueStyle =
      TextStyle(
        fontSize = 11.sp,
        color = if (isSystemInDarkTheme) Color.White.copy(alpha = 0.78f) else Color(0xFF1B1E25).copy(alpha = 0.7f),
      )

    stages.forEachIndexed { index, stage ->
      val topFull = widthFor(stage.value)
      val bottomValue = if (index < count - 1) stages[index + 1].value else stage.value
      val bottomFull = widthFor(bottomValue)

      // Animate widths expanding from the center.
      val topHalf = (topFull / 2f) * animationProgress
      val bottomHalf = (bottomFull / 2f) * animationProgress

      val bandTop = chartTop + index * (bandHeight + gap)
      val bandBottom = bandTop + bandHeight

      val path =
        Path().apply {
          moveTo(centerX - topHalf, bandTop)
          lineTo(centerX + topHalf, bandTop)
          lineTo(centerX + bottomHalf, bandBottom)
          lineTo(centerX - bottomHalf, bandBottom)
          close()
        }

      val brush =
        Brush.verticalGradient(
          colors =
          listOf(
            stage.color.copy(alpha = 0.95f * animationProgress),
            stage.color.copy(alpha = 0.7f * animationProgress),
          ),
          startY = bandTop,
          endY = bandBottom,
        )
      drawScope.drawPath(path = path, brush = brush)
      // Soft top highlight for a rounded, premium feel.
      drawScope.drawLine(
        color = Color.White.copy(alpha = 0.22f * animationProgress),
        start = Offset(centerX - topHalf, bandTop),
        end = Offset(centerX + topHalf, bandTop),
        strokeWidth = 1.5f,
      )

      if (animationProgress > 0.55f) {
        val centerY = bandTop + bandHeight / 2f
        val labelLayout: TextLayoutResult = textMeasurer.measure(stage.label, labelStyle)
        val valueLayout: TextLayoutResult = textMeasurer.measure(formatValue(stage.value), valueStyle)
        val totalH = labelLayout.size.height + valueLayout.size.height + 2f

        drawScope.drawText(
          textMeasurer = textMeasurer,
          text = stage.label,
          style = labelStyle,
          topLeft =
          Offset(
            centerX - labelLayout.size.width / 2f,
            centerY - totalH / 2f,
          ),
        )
        drawScope.drawText(
          textMeasurer = textMeasurer,
          text = formatValue(stage.value),
          style = valueStyle,
          topLeft =
          Offset(
            centerX - valueLayout.size.width / 2f,
            centerY - totalH / 2f + labelLayout.size.height + 2f,
          ),
        )
      }
    }
  }

  private fun formatValue(value: Float): String {
    val rounded = (value * 100f).toInt() / 100f
    return if (rounded % 1f == 0f) rounded.toInt().toString() else rounded.toString()
  }
}
