package io.androidpoet.drafter.stepline

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.Offset
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
import io.androidpoet.drafter.internal.areaGradient
import io.androidpoet.drafter.internal.drawVertexDot
import io.androidpoet.drafter.stepline.model.StepLineChartData
import io.androidpoet.drafter.theme.DrafterColors

@Immutable
public class StepLineChartRenderer(
  public val data: StepLineChartData,
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

    // Horizontal gridlines + y labels.
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

    // Build the stepped path: horizontal to next x, then vertical to next y.
    val stepPath = Path()
    stepPath.moveTo(points.first().x, points.first().y)
    for (i in 1 until points.size) {
      stepPath.lineTo(points[i].x, points[i - 1].y)
      stepPath.lineTo(points[i].x, points[i].y)
    }

    val clamped = animationProgress.coerceIn(0f, 1f)
    val revealRight = chartLeft + chartWidth * clamped

    // Subtle translucent fill below the steps.
    val fillPath =
      Path().apply {
        addPath(stepPath)
        lineTo(points.last().x, chartBottom)
        lineTo(points.first().x, chartBottom)
        close()
      }
    val topY = points.minOf { it.y }
    drawScope.clipRect(right = revealRight) {
      drawPath(
        path = fillPath,
        brush = areaGradient(data.color, topY, chartBottom, topAlpha = 0.22f),
        style = Fill,
      )
      // Stepped line with rounded caps/joins.
      drawPath(
        path = stepPath,
        color = data.color,
        style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round),
      )
    }

    // Vertex dots at each data point.
    points.forEach { point ->
      if (point.x <= revealRight + 0.5f) {
        drawScope.drawVertexDot(point, data.color, radius = 4f)
      }
    }

    // X-axis labels.
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
