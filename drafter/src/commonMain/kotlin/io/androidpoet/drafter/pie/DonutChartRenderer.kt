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
package io.androidpoet.drafter.pie

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

public class DonutChartRenderer(
  private val data: PieChartData,
  private val size: Size,
  private val textMeasurer: TextMeasurer,
  private val animationProgress: Float = 1f,
  private val labelThreshold: Float = 5f,
  private val holeRadiusFraction: Float = 0.5f,
) {
  public fun drawDonutChart(drawScope: DrawScope) {
    val totalValue = data.slices.sumOf { it.value.toDouble() }
    var startAngle = -90f
    val outerRadius = size.minDimension / 2 * 0.5f // 50% of half the min dimension
    val innerRadius = outerRadius * holeRadiusFraction
    val center = Offset(size.width / 2, size.height / 2)
    val labelRadius = outerRadius * 1.4f // Position labels outside the donut

    data.slices.forEach { slice ->
      val sweepAngle = (slice.value / totalValue * 360 * animationProgress).toFloat()

      // Draw the donut slice
      drawScope.drawArc(
        color = slice.color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
        size = Size(outerRadius * 2, outerRadius * 2),
        style = Stroke(width = outerRadius - innerRadius),
      )

      val percentage = (slice.value / totalValue * 100).toFloat()

      // Draw labels if the percentage exceeds the threshold and animation progress allows
      if (percentage >= labelThreshold && (animationProgress == 1f || sweepAngle > 0f)) {
        val labelText = "${percentage.toInt()}%"

        val angleInRadians = (startAngle + sweepAngle / 2) * (PI.toFloat() / 180f)
        val labelX = center.x + (labelRadius * cos(angleInRadians))
        val labelY = center.y + (labelRadius * sin(angleInRadians))

        val style = TextStyle(fontSize = 12.sp, color = Color.Black)
        val textLayoutResult = textMeasurer.measure(labelText, style)

        // Adjust label position to avoid overlapping with the donut
        val adjustedLabelX = if (labelX < center.x) {
          labelX - textLayoutResult.size.width - 5f
        } else {
          labelX + 5f
        }
        val adjustedLabelY = if (labelY < center.y) {
          labelY - textLayoutResult.size.height - 5f
        } else {
          labelY + 5f
        }

        // Check if the adjusted position is within the drawable area
        if (adjustedLabelX >= 0 && adjustedLabelY >= 0 &&
          adjustedLabelX + textLayoutResult.size.width <= size.width &&
          adjustedLabelY + textLayoutResult.size.height <= size.height
        ) {
          drawScope.drawText(
            textMeasurer = textMeasurer,
            text = labelText,
            style = style,
            topLeft = Offset(adjustedLabelX, adjustedLabelY),
          )
        }
      }

      startAngle += sweepAngle
    }
  }
}
