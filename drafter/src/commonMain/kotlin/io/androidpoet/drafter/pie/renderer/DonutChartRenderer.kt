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
package io.androidpoet.drafter.pie.renderer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.pie.model.PieChartData
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Renders a "donut" by using an arc + Stroke (hole in center).
 */
public class DonutChartRenderer(
  override val data: PieChartData,
  private val labelThreshold: Float = 5f,
  private val holeRadiusFraction: Float = 0.5f,
) : PieChartDataRenderer {
  public override fun drawChart(
    drawScope: DrawScope,
    size: Size,
    progress: Float,
    textMeasurer: TextMeasurer,
  ) {
    val totalValue =
      max(
        data.slices.sumOf { slice -> slice.value.toDouble() }.toFloat(),
        1f,
      )
    var startAngle = -90f

    val outerRadius = (size.minDimension / 2) * 0.6f
    val innerRadius = outerRadius * holeRadiusFraction
    val center = Offset(size.width / 2, size.height / 2)
    val labelRadius = outerRadius * 1.3f

    data.slices.forEach { slice ->
      val slicePercentage = slice.value / totalValue
      val sweepAngle = slicePercentage * 360f * progress

      // Draw donut slice
      drawScope.drawArc(
        color = slice.color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
        size = Size(outerRadius * 2, outerRadius * 2),
        style = Stroke(width = outerRadius - innerRadius),
      )

      // Optionally label the slice
      val percentage = slicePercentage * 100
      if (percentage >= labelThreshold && sweepAngle > 0f) {
        val midAngleRad = (startAngle + sweepAngle / 2) * (PI.toFloat() / 180f)
        val labelRadius = outerRadius * 1.3f

        val labelText = "${percentage.toInt()}%"
        val style = TextStyle(fontSize = 12.sp, color = Color.Black)
        val textLayout = textMeasurer.measure(labelText, style)

        // Calculate the base position on the circle
        val baseX = center.x + (labelRadius * cos(midAngleRad))
        val baseY = center.y + (labelRadius * sin(midAngleRad))

        // Center text around the point on circle while maintaining radial alignment
        val xOffset = -textLayout.size.width / 2
        val yOffset = -textLayout.size.height / 2

        // Add slight outward push in the direction of the radius
        val radialPushFactor = 0.15f // Adjust this value to control how far text pushes outward
        val radialOffsetX = radialPushFactor * labelRadius * cos(midAngleRad)
        val radialOffsetY = radialPushFactor * labelRadius * sin(midAngleRad)

        drawScope.drawText(
          textMeasurer = textMeasurer,
          text = labelText,
          style = style,
          topLeft =
            Offset(
              x = baseX + xOffset + radialOffsetX,
              y = baseY + yOffset + radialOffsetY,
            ),
        )
      }

      startAngle += sweepAngle
    }
  }
}
