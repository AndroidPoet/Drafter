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
package io.androidpoet.drafter.internal

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect

/**
 * Internal drawing helpers shared across the chart renderers.
 *
 * These give every chart the same "smooth, premium" character: Catmull-Rom
 * cubic-bezier curves instead of jagged segments, soft fade-to-transparent
 * gradient fills, and a left-to-right reveal animation that traces the curve.
 */

/**
 * Builds a smooth cubic-bezier [Path] that passes through every [points] vertex
 * using a Catmull-Rom spline. Falls back to straight segments when there are
 * fewer than three points (a curve is undefined there).
 */
internal fun smoothPath(points: List<Offset>): Path {
  val path = Path()
  if (points.isEmpty()) return path

  path.moveTo(points[0].x, points[0].y)
  if (points.size < 3) {
    for (i in 1 until points.size) path.lineTo(points[i].x, points[i].y)
    return path
  }

  for (i in 0 until points.size - 1) {
    val p0 = points[if (i - 1 < 0) i else i - 1]
    val p1 = points[i]
    val p2 = points[i + 1]
    val p3 = points[if (i + 2 > points.size - 1) i + 1 else i + 2]

    // Catmull-Rom -> cubic bezier control points (tension 0.5).
    val c1x = p1.x + (p2.x - p0.x) / 6f
    val c1y = p1.y + (p2.y - p0.y) / 6f
    val c2x = p2.x - (p3.x - p1.x) / 6f
    val c2y = p2.y - (p3.y - p1.y) / 6f

    path.cubicTo(c1x, c1y, c2x, c2y, p2.x, p2.y)
  }
  return path
}

/** A soft vertical gradient fading from [color] near the curve to transparent at the baseline. */
internal fun areaGradient(
  color: Color,
  top: Float,
  bottom: Float,
  topAlpha: Float = 0.32f,
): Brush =
  Brush.verticalGradient(
    colors =
    listOf(
      color.copy(alpha = topAlpha),
      color.copy(alpha = topAlpha * 0.45f),
      color.copy(alpha = 0f),
    ),
    startY = top,
    endY = bottom,
  )

/**
 * Draws a single smooth line series with an optional gradient area fill, a
 * tracing left-to-right reveal animation, and an optional highlighted end dot.
 *
 * @param points data vertices in pixel space, left to right
 * @param color stroke colour for the line
 * @param baseline y-coordinate the area fill drops down to (chart bottom)
 * @param progress reveal progress in [0,1]
 * @param strokeWidth line thickness in pixels
 * @param fill when true, paints the soft gradient area under the curve
 * @param endDot when true, draws a glowing dot at the leading edge of the reveal
 */
internal fun DrawScope.drawSmoothLine(
  points: List<Offset>,
  color: Color,
  baseline: Float,
  progress: Float,
  strokeWidth: Float = 6f,
  fill: Boolean = true,
  endDot: Boolean = true,
) {
  if (points.size < 2) return
  val clamped = progress.coerceIn(0f, 1f)
  val linePath = smoothPath(points)

  val startX = points.first().x
  val endX = points.last().x
  val revealRight = startX + (endX - startX) * clamped

  if (fill) {
    val topY = points.minOf { it.y }
    val fillPath =
      Path().apply {
        addPath(linePath)
        lineTo(endX, baseline)
        lineTo(startX, baseline)
        close()
      }
    clipRect(right = revealRight) {
      drawPath(
        path = fillPath,
        brush = areaGradient(color, topY, baseline),
        style = Fill,
      )
    }
  }

  // Trace the stroke exactly up to the reveal frontier for a clean "drawing" feel.
  val measure = PathMeasure().apply { setPath(linePath, false) }
  val drawn = Path()
  val drewSomething = measure.getSegment(0f, measure.length * clamped, drawn, true)
  if (drewSomething || clamped <= 0f) {
    drawPath(
      path = drawn,
      color = color,
      style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
  }

  if (endDot && clamped > 0.001f) {
    val pos = measure.getPosition(measure.length * clamped)
    drawCircle(color = Color.White, radius = strokeWidth * 1.5f, center = pos)
    drawCircle(color = color, radius = strokeWidth * 0.95f, center = pos)
  }
}

/** Draws a small filled dot with a white halo — used to mark line vertices. */
internal fun DrawScope.drawVertexDot(
  center: Offset,
  color: Color,
  radius: Float,
) {
  drawCircle(color = Color.White, radius = radius * 1.7f, center = center)
  drawCircle(color = color, radius = radius, center = center)
}
