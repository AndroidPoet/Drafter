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
package io.androidpoet.drafter.radar.renderer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.internal.drawVertexDot
import io.androidpoet.drafter.radar.RadarChartDataRenderer
import io.androidpoet.drafter.radar.model.RadarChartData
import io.androidpoet.drafter.theme.DrafterColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

public class RadarChartRenderer(
  public val data: List<RadarChartData>,
  public val colors: List<Color>,
) : RadarChartDataRenderer {
  override fun drawChart(
    drawScope: DrawScope,
    centerX: Float,
    centerY: Float,
    radius: Float,
    textMeasurer: TextMeasurer,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
  ) {
    drawBackgroundAndAxes(
      drawScope,
      centerX,
      centerY,
      radius,
      textMeasurer,
      data
        .first()
        .values.keys
        .toList(),
      isSystemInDarkTheme,
    )
    data.forEachIndexed { index, chartData ->
      drawDataPolygon(
        drawScope,
        centerX,
        centerY,
        radius,
        chartData,
        colors[index],
        animationProgress,
      )
    }
  }

  private fun drawBackgroundAndAxes(
    drawScope: DrawScope,
    centerX: Float,
    centerY: Float,
    radius: Float,
    textMeasurer: TextMeasurer,
    axisLabels: List<String>,
    isSystemInDarkTheme: Boolean,
  ) {
    val numberOfAxes = axisLabels.size
    val gridColor = if (isSystemInDarkTheme) DrafterColors.GridDark else DrafterColors.GridLight
    val labelColor = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight
    for (i in 1..5) {
      drawScope.drawCircle(
        color = gridColor,
        center = Offset(centerX, centerY),
        radius = radius * i / 5,
        style = Stroke(width = 1f),
      )
    }
    for (i in 0 until numberOfAxes) {
      val angle = i * 2 * PI / numberOfAxes - PI / 2
      val endX = centerX + radius * cos(angle).toFloat()
      val endY = centerY + radius * sin(angle).toFloat()

      drawScope.drawLine(
        color = gridColor,
        start = Offset(centerX, centerY),
        end = Offset(endX, endY),
        strokeWidth = 1f,
      )
      val textLayoutResult =
        textMeasurer.measure(
          text = axisLabels[i],
          style =
          TextStyle(
            color = labelColor,
            fontSize = 12.sp,
          ),
        )

      drawScope.drawText(
        textLayoutResult = textLayoutResult,
        topLeft =
        Offset(
          x = endX - textLayoutResult.size.width / 2,
          y = endY - textLayoutResult.size.height / 2,
        ),
      )
    }
  }

  private fun drawDataPolygon(
    drawScope: DrawScope,
    centerX: Float,
    centerY: Float,
    radius: Float,
    data: RadarChartData,
    color: Color,
    progress: Float,
  ) {
    val points =
      data.values.values.mapIndexed { index, value ->
        val angle = index * 2 * PI / data.values.size - PI / 2
        val x = centerX + radius * value * progress * cos(angle).toFloat()
        val y = centerY + radius * value * progress * sin(angle).toFloat()
        Offset(x, y)
      }

    val path =
      Path().apply {
        moveTo(points.first().x, points.first().y)
        points.forEach { lineTo(it.x, it.y) }
        close()
      }

    drawScope.drawPath(
      path = path,
      color = color.copy(alpha = 0.22f * progress),
      style = Fill,
    )

    drawScope.drawPath(
      path = path,
      color = color.copy(alpha = 0.9f * progress),
      style = Stroke(width = 2.5f, join = StrokeJoin.Round),
    )

    // Mark each vertex with a haloed dot once the polygon has expanded enough.
    if (progress > 0.6f) {
      points.forEach { drawScope.drawVertexDot(it, color, radius = 4f) }
    }
  }
}
