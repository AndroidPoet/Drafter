package io.androidpoet.drafter.area

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.area.model.AreaChartData
import io.androidpoet.drafter.internal.areaGradient
import io.androidpoet.drafter.internal.drawVertexDot
import io.androidpoet.drafter.internal.smoothPath
import io.androidpoet.drafter.theme.DrafterColors

@Immutable
public class AreaChartRenderer(
  public val data: AreaChartData,
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
    if (data.values.isEmpty()) return

    val chartBottom = chartTop + chartHeight
    val chartRight = chartLeft + chartWidth
    val maxValue = (data.values.maxOrNull() ?: 0f).let { if (it <= 0f) 1f else it }

    val gridColor = if (isSystemInDarkTheme) DrafterColors.GridDark else DrafterColors.GridLight
    val labelColor = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight
    val labelStyle = TextStyle(fontSize = 10.sp, color = labelColor)

    // Horizontal gridlines + y labels (a few evenly-spaced ticks 0..max).
    val tickCount = 4
    for (i in 0..tickCount) {
      val fraction = i.toFloat() / tickCount
      val y = chartBottom - fraction * chartHeight
      drawScope.drawLine(
        color = gridColor,
        start = Offset(chartLeft, y),
        end = Offset(chartRight, y),
        strokeWidth = 1f,
      )
      val value = maxValue * fraction
      val label = formatTick(value)
      val measured = textMeasurer.measure(label, labelStyle)
      drawScope.drawText(
        textMeasurer = textMeasurer,
        text = label,
        style = labelStyle,
        topLeft = Offset(chartLeft - measured.size.width - 6f, y - measured.size.height / 2f),
      )
    }

    // Map data points to pixel space.
    val points =
      data.values.mapIndexed { index, value ->
        val x =
          if (data.values.size == 1) {
            chartLeft + chartWidth / 2f
          } else {
            chartLeft + (index.toFloat() / (data.values.size - 1)) * chartWidth
          }
        val y = chartBottom - (value / maxValue) * chartHeight
        Offset(x, y)
      }

    val clamped = animationProgress.coerceIn(0f, 1f)
    val revealRight = chartLeft + chartWidth * clamped

    val linePath = smoothPath(points)
    val topY = points.minOf { it.y }

    // Soft gradient fill down to the baseline.
    val fillPath =
      Path().apply {
        addPath(linePath)
        lineTo(points.last().x, chartBottom)
        lineTo(points.first().x, chartBottom)
        close()
      }
    drawScope.clipRect(right = revealRight) {
      drawPath(
        path = fillPath,
        brush = areaGradient(data.color, topY, chartBottom),
        style = Fill,
      )
      // Stroke the top curve.
      drawPath(
        path = linePath,
        color = data.color,
        style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round),
      )
    }

    // White-haloed vertex dots, revealed with the sweep.
    points.forEach { point ->
      if (point.x <= revealRight + 0.5f) {
        drawScope.drawVertexDot(point, data.color, radius = 4f)
      }
    }

    // X-axis labels.
    drawXLabels(drawScope, points, chartBottom, textMeasurer, labelStyle)
  }

  private fun drawXLabels(
    drawScope: DrawScope,
    points: List<Offset>,
    chartBottom: Float,
    textMeasurer: TextMeasurer,
    labelStyle: TextStyle,
  ) {
    points.forEachIndexed { index, point ->
      val label = data.labels.getOrNull(index) ?: return@forEachIndexed
      val measured = textMeasurer.measure(label, labelStyle)
      drawScope.drawText(
        textMeasurer = textMeasurer,
        text = label,
        style = labelStyle,
        topLeft = Offset(point.x - measured.size.width / 2f, chartBottom + 6f),
      )
    }
  }

  private fun formatTick(value: Float): String {
    val rounded = value.toInt()
    return if (value == rounded.toFloat()) rounded.toString() else ((value * 10).toInt() / 10f).toString()
  }
}
