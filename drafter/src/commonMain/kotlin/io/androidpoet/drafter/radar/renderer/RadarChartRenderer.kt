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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.radar.RadarChartDataRenderer
import io.androidpoet.drafter.radar.model.RadarChartData
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
    // Draw background circles and axes
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

    // Draw data polygons with animation
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

    // Draw background circles
    for (i in 1..10) {
      drawScope.drawCircle(
        color = if (isSystemInDarkTheme) Color.White else Color.Black,
        center = Offset(centerX, centerY),
        radius = radius * i / 10,
        style = Stroke(width = 1f),
      )
    }

    // Draw axes
    for (i in 0 until numberOfAxes) {
      val angle = i * 2 * PI / numberOfAxes - PI / 2
      val endX = centerX + radius * cos(angle).toFloat()
      val endY = centerY + radius * sin(angle).toFloat()

      drawScope.drawLine(
        color = if (isSystemInDarkTheme) Color.White else Color.Black,
        start = Offset(centerX, centerY),
        end = Offset(endX, endY),
        strokeWidth = 1f,
      )

      // Draw axis labels
      val textLayoutResult =
        textMeasurer.measure(
          text = axisLabels[i],
          style =
          TextStyle(
            color = if (isSystemInDarkTheme) Color.White else Color.Black,
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
      color = color.copy(alpha = 0.5f * progress),
      style = Fill,
    )

    drawScope.drawPath(
      path = path,
      color = color.copy(alpha = progress),
      style = Stroke(width = 2f),
    )
  }
}
