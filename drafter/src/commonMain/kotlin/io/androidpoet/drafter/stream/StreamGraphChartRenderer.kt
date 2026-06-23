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
package io.androidpoet.drafter.stream

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.internal.smoothPath
import io.androidpoet.drafter.stream.model.StreamData
import io.androidpoet.drafter.theme.DrafterColors

/**
 * Renders a stream graph (themeriver): each series flows as a smooth band, the
 * whole stack centred around a wiggle baseline so it reads like a river.
 */
@Immutable
public class StreamGraphChartRenderer(
  public val data: StreamData,
) : io.androidpoet.drafter.core.ChartRenderer {
  /** Number of x points shared by every series. */
  private val pointCount: Int =
    maxOf(data.labels.size, data.series.minOfOrNull { it.values.size } ?: 0)

  /** The largest total stacked value across all x points (drives the y scale). */
  private fun maxTotal(): Float {
    var max = 0f
    for (i in 0 until pointCount) {
      var total = 0f
      for (s in data.series) total += s.values.getOrElse(i) { 0f }
      if (total > max) max = total
    }
    return max
  }

  /**
   * Draws the stream graph into [drawScope] within the given bounds.
   *
   * @param animationProgress vertical growth from the centre baseline in [0,1]
   */
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
    if (pointCount < 2 || data.series.isEmpty()) return

    val progress = animationProgress.coerceIn(0f, 1f)
    val centerY = chartTop + chartHeight / 2f
    val maxTotal = maxTotal()
    if (maxTotal <= 0f) return

    // Fit the tallest total stack into ~80% of the available height.
    val yScale = (chartHeight * 0.8f) / maxTotal
    val stepX = if (pointCount > 1) chartWidth / (pointCount - 1) else chartWidth

    // Pre-compute the centred baseline (top edge of the whole stack) at each x.
    // Scale the half-height by progress so the stack grows outward from the
    // centre baseline rather than dropping in from a fixed top edge.
    val xs = FloatArray(pointCount) { chartLeft + it * stepX }
    val stackTop = FloatArray(pointCount)
    for (i in 0 until pointCount) {
      var total = 0f
      for (s in data.series) total += s.values.getOrElse(i) { 0f }
      val halfHeight = (total * yScale * progress) / 2f
      stackTop[i] = centerY - halfHeight
    }

    // Running cumulative top per x; each series stacks below the previous one.
    val runningTop = stackTop.copyOf()

    data.series.forEach { series ->
      val topEdge = ArrayList<Offset>(pointCount)
      val bottomEdge = ArrayList<Offset>(pointCount)

      for (i in 0 until pointCount) {
        val thickness = series.values.getOrElse(i) { 0f } * yScale * progress
        val top = runningTop[i]
        val bottom = top + thickness
        topEdge.add(Offset(xs[i], top))
        bottomEdge.add(Offset(xs[i], bottom))
        // Advance the cumulative baseline for the next series at this x.
        runningTop[i] = bottom
      }

      drawScope.drawBand(topEdge, bottomEdge, series.color)
    }

    drawScope.drawXLabels(textMeasurer, xs, chartTop + chartHeight, isSystemInDarkTheme)
  }

  /** Builds a closed band from a smooth top edge and a smooth bottom edge, then fills it. */
  private fun DrawScope.drawBand(
    topEdge: List<Offset>,
    bottomEdge: List<Offset>,
    color: Color,
  ) {
    if (topEdge.size < 2) return

    val topPath = smoothPath(topEdge)
    val reversedBottom = bottomEdge.asReversed()

    // Build the closed band: smooth top edge L->R, line down to the bottom edge,
    // smooth bottom edge R->L, then close back to the start.
    val band = smoothPath(topEdge)
    appendSmoothInto(band, reversedBottom)
    band.close()

    val minY = topEdge.minOf { it.y }
    val maxY = bottomEdge.maxOf { it.y }
    val gradient: Brush =
      Brush.verticalGradient(
        colors =
        listOf(
          color.copy(alpha = 0.92f),
          color.copy(alpha = 0.78f),
        ),
        startY = minY,
        endY = maxY,
      )

    drawPath(path = band, color = color.copy(alpha = 0.85f), style = Fill)
    drawPath(path = band, brush = gradient, style = Fill)

    // Thin lighter stroke along the top edge for separation between bands.
    drawPath(
      path = topPath,
      color = Color.White.copy(alpha = 0.22f),
      style = Stroke(width = 1.5f, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
  }

  /**
   * Appends a Catmull-Rom smooth curve through [points] into [path], continuing
   * the existing subpath: lines to the first point, then cubic segments through
   * the rest (mirrors [smoothPath] but without starting a new subpath).
   */
  private fun appendSmoothInto(
    path: Path,
    points: List<Offset>,
  ) {
    if (points.isEmpty()) return
    path.lineTo(points[0].x, points[0].y)
    if (points.size < 3) {
      for (i in 1 until points.size) path.lineTo(points[i].x, points[i].y)
      return
    }
    for (i in 0 until points.size - 1) {
      val p0 = points[if (i - 1 < 0) i else i - 1]
      val p1 = points[i]
      val p2 = points[i + 1]
      val p3 = points[if (i + 2 > points.size - 1) i + 1 else i + 2]
      val c1x = p1.x + (p2.x - p0.x) / 6f
      val c1y = p1.y + (p2.y - p0.y) / 6f
      val c2x = p2.x - (p3.x - p1.x) / 6f
      val c2y = p2.y - (p3.y - p1.y) / 6f
      path.cubicTo(c1x, c1y, c2x, c2y, p2.x, p2.y)
    }
  }

  /** Draws a sparse set of x labels along the bottom of the chart. */
  private fun DrawScope.drawXLabels(
    textMeasurer: TextMeasurer,
    xs: FloatArray,
    baseline: Float,
    isSystemInDarkTheme: Boolean,
  ) {
    if (data.labels.isEmpty()) return
    val style =
      TextStyle(
        fontSize = 10.sp,
        color = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight,
      )

    val maxLabels = 6
    val count = data.labels.size
    val stride = maxOf(1, (count + maxLabels - 1) / maxLabels)

    for (i in data.labels.indices step stride) {
      if (i >= xs.size) break
      val label = data.labels[i]
      val layout = textMeasurer.measure(label, style)
      drawText(
        textMeasurer = textMeasurer,
        text = label,
        style = style,
        topLeft =
        Offset(
          xs[i] - layout.size.width / 2f,
          baseline + 6f,
        ),
      )
    }
  }
}
