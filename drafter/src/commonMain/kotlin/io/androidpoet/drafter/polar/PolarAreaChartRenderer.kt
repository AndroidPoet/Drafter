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
package io.androidpoet.drafter.polar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.polar.model.PolarAreaData
import io.androidpoet.drafter.theme.DrafterColors
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

public interface PolarAreaRenderer : io.androidpoet.drafter.core.ChartRenderer {
  public fun maxValue(): Float

  public fun draw(
    drawScope: DrawScope,
    centerX: Float,
    centerY: Float,
    maxRadius: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  )
}

@Immutable
public class PolarAreaChartRenderer(
  public val data: PolarAreaData,
) : PolarAreaRenderer {
  override fun maxValue(): Float = data.slices.maxOfOrNull { it.value } ?: 0f

  override fun draw(
    drawScope: DrawScope,
    centerX: Float,
    centerY: Float,
    maxRadius: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  ) {
    val slices = data.slices
    if (slices.isEmpty() || maxRadius <= 0f) return

    val maxValue = max(maxValue(), 0.0001f)
    val center = Offset(centerX, centerY)
    val gridColor =
      if (isSystemInDarkTheme) DrafterColors.GridDark else DrafterColors.GridLight
    val sweepPer = 360f / slices.size

    drawScope.drawGrid(center, maxRadius, slices.size, gridColor, sweepPer)

    // Wedges: equal angle, radius proportional to value.
    slices.forEachIndexed { index, slice ->
      val startAngle = -90f + index * sweepPer
      val targetRadius = (slice.value / maxValue) * maxRadius
      val radius = targetRadius * animationProgress
      if (radius <= 0f) return@forEachIndexed

      val topLeft = Offset(center.x - radius, center.y - radius)
      val arcSize = Size(radius * 2f, radius * 2f)

      drawScope.drawArc(
        color = slice.color.copy(alpha = 0.7f),
        startAngle = startAngle,
        sweepAngle = sweepPer,
        useCenter = true,
        topLeft = topLeft,
        size = arcSize,
      )
      drawScope.drawArc(
        color = Color.White.copy(alpha = 0.55f),
        startAngle = startAngle,
        sweepAngle = sweepPer,
        useCenter = true,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = 1.5f),
      )
    }

    drawScope.drawPolarLabels(
      center = center,
      maxRadius = maxRadius,
      labels = slices.map { it.label },
      sweepPer = sweepPer,
      isSystemInDarkTheme = isSystemInDarkTheme,
      textMeasurer = textMeasurer,
    )
  }
}

internal fun DrawScope.drawGrid(
  center: Offset,
  maxRadius: Float,
  sliceCount: Int,
  gridColor: Color,
  sweepPer: Float,
) {
  val rings = 4
  for (ring in 1..rings) {
    val r = maxRadius * ring / rings
    drawCircle(
      color = gridColor,
      radius = r,
      center = center,
      style = Stroke(width = 1f),
    )
  }
  // Radial spokes along each wedge boundary.
  for (i in 0 until sliceCount) {
    val angle = ((-90f + i * sweepPer) * (kotlin.math.PI / 180f)).toFloat()
    drawLine(
      color = gridColor,
      start = center,
      end = Offset(center.x + cos(angle) * maxRadius, center.y + sin(angle) * maxRadius),
      strokeWidth = 1f,
    )
  }
}

internal fun DrawScope.drawPolarLabels(
  center: Offset,
  maxRadius: Float,
  labels: List<String>,
  sweepPer: Float,
  isSystemInDarkTheme: Boolean,
  textMeasurer: TextMeasurer,
) {
  val style =
    TextStyle(
      fontSize = 10.sp,
      color = if (isSystemInDarkTheme) Color.White else Color.Black,
    )
  labels.forEachIndexed { index, label ->
    val midAngleDeg = -90f + index * sweepPer + sweepPer / 2f
    val midAngle = (midAngleDeg * (kotlin.math.PI / 180f)).toFloat()
    val labelRadius = maxRadius + 18f
    val lx = center.x + cos(midAngle) * labelRadius
    val ly = center.y + sin(midAngle) * labelRadius
    val layout = textMeasurer.measure(label, style)
    drawText(
      textMeasurer = textMeasurer,
      text = label,
      style = style,
      topLeft = Offset(lx - layout.size.width / 2f, ly - layout.size.height / 2f),
    )
  }
}
