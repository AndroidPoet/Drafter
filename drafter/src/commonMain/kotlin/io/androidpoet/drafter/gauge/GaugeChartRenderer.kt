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
package io.androidpoet.drafter.gauge

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.gauge.model.GaugeData
import io.androidpoet.drafter.theme.DrafterColors
import kotlin.math.cos
import kotlin.math.sin

@Immutable
public class GaugeChartRenderer(
  public val data: GaugeData,
) : io.androidpoet.drafter.core.ChartRenderer {
  private val startAngle: Float = 150f
  private val sweepAngle: Float = 240f

  public fun draw(
    drawScope: DrawScope,
    centerX: Float,
    centerY: Float,
    radius: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  ) {
    val strokeWidth = radius * 0.16f
    val arcRadius = radius - strokeWidth / 2f
    val topLeft = Offset(centerX - arcRadius, centerY - arcRadius)
    val arcSize = Size(arcRadius * 2f, arcRadius * 2f)

    val trackColor = if (isSystemInDarkTheme) DrafterColors.GridDark else DrafterColors.GridLight

    // Background track arc.
    drawScope.drawArc(
      color = trackColor,
      startAngle = startAngle,
      sweepAngle = sweepAngle,
      useCenter = false,
      topLeft = topLeft,
      size = arcSize,
      style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )

    // Value fraction clamped to [0, 1].
    val span = (data.max - data.min).takeIf { it != 0f } ?: 1f
    val rawFraction = ((data.value - data.min) / span).coerceIn(0f, 1f)
    val fraction = rawFraction * animationProgress
    val valueSweep = sweepAngle * fraction

    if (valueSweep > 0f) {
      // Sweep gradient across the palette gives the value arc a premium multi-tone look.
      val brush =
        Brush.sweepGradient(
          colors = DrafterColors.palette + DrafterColors.palette.first(),
          center = Offset(centerX, centerY),
        )
      drawScope.drawArc(
        brush = brush,
        startAngle = startAngle,
        sweepAngle = valueSweep,
        useCenter = false,
        topLeft = topLeft,
        size = arcSize,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
      )
    }

    // Knob at the tip of the value arc.
    val tipAngleRad = (startAngle + valueSweep).toRadians()
    val tip = Offset(centerX + arcRadius * cos(tipAngleRad), centerY + arcRadius * sin(tipAngleRad))
    drawScope.drawCircle(
      color = Color.White,
      radius = strokeWidth * 0.42f,
      center = tip,
    )
    drawScope.drawCircle(
      color = data.color,
      radius = strokeWidth * 0.42f,
      center = tip,
      style = Stroke(width = 2f),
    )

    // Center numeric value (big) + label below.
    val valueText = formatValue(data.value)
    val valueStyle =
      TextStyle(
        fontSize = (radius * 0.04f).coerceIn(20f, 44f).sp,
        color = if (isSystemInDarkTheme) Color.White else Color(0xFF1B1E25),
      )
    val labelStyle =
      TextStyle(
        fontSize = 13.sp,
        color = if (isSystemInDarkTheme) {
          Color.White.copy(
            alpha = 0.72f,
          )
        } else {
          Color(0xFF1B1E25).copy(alpha = 0.6f)
        },
      )

    val valueLayout: TextLayoutResult = textMeasurer.measure(valueText, valueStyle)
    val hasLabel = data.label.isNotEmpty()
    val labelLayout: TextLayoutResult? = if (hasLabel) {
      textMeasurer.measure(
        data.label,
        labelStyle,
      )
    } else {
      null
    }
    val labelH = labelLayout?.size?.height ?: 0
    val totalH = valueLayout.size.height + (if (hasLabel) labelH + 6 else 0)
    val blockTop = centerY - totalH / 2f

    drawScope.drawText(
      textMeasurer = textMeasurer,
      text = valueText,
      style = valueStyle,
      topLeft = Offset(centerX - valueLayout.size.width / 2f, blockTop),
    )
    if (labelLayout != null) {
      drawScope.drawText(
        textMeasurer = textMeasurer,
        text = data.label,
        style = labelStyle,
        topLeft = Offset(
          centerX - labelLayout.size.width / 2f,
          blockTop + valueLayout.size.height + 6f,
        ),
      )
    }

    // Min / max end labels.
    val endStyle =
      TextStyle(
        fontSize = 11.sp,
        color = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight,
      )
    drawEndLabel(
      drawScope, textMeasurer,
      formatValue(
        data.min,
      ),
      startAngle, centerX, centerY, arcRadius, strokeWidth, endStyle,
    )
    drawEndLabel(
      drawScope, textMeasurer,
      formatValue(
        data.max,
      ),
      startAngle + sweepAngle, centerX, centerY, arcRadius, strokeWidth, endStyle,
    )
  }

  private fun drawEndLabel(
    drawScope: DrawScope,
    textMeasurer: TextMeasurer,
    text: String,
    angleDeg: Float,
    centerX: Float,
    centerY: Float,
    arcRadius: Float,
    strokeWidth: Float,
    style: TextStyle,
  ) {
    val rad = angleDeg.toRadians()
    val r = arcRadius + strokeWidth * 0.9f
    val px = centerX + r * cos(rad)
    val py = centerY + r * sin(rad)
    val layout = textMeasurer.measure(text, style)
    drawScope.drawText(
      textMeasurer = textMeasurer,
      text = text,
      style = style,
      topLeft = Offset(px - layout.size.width / 2f, py - layout.size.height / 2f),
    )
  }

  private fun Float.toRadians(): Float = this * (kotlin.math.PI.toFloat() / 180f)

  private fun formatValue(value: Float): String = io.androidpoet.drafter.core.formatChartValue(
    value,
    decimals = 2,
  )
}
