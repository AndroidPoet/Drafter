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
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

public class PieChartRenderer(
  private val data: PieChartData,
  private val size: Size,
  private val textMeasurer: TextMeasurer,
  private val animationProgress: Float = 1f,
  private val labelThreshold: Float = 5f,
) {
  public fun drawPieChart(drawScope: DrawScope) {
    val totalValue = data.slices.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = -90f
    val radius = size.minDimension / 2
    val center = Offset(size.width / 2, size.height / 2)

    data.slices.forEach { slice ->
      val sweepAngle = (slice.value / totalValue) * 360f * animationProgress

      // Draw the slice
      drawScope.drawArc(
        color = slice.color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        topLeft = Offset.Zero,
        size = size,
      )

      val percentage = (slice.value / totalValue) * 100

      if (percentage >= labelThreshold && (animationProgress == 1f || sweepAngle > 0f)) {
        val labelText = "${percentage.toInt()}%"

        val angleInRadians = (startAngle + sweepAngle / 2) * (PI / 180f)
        val labelRadius = radius * 0.7f
        val labelX = center.x + (labelRadius * cos(angleInRadians)).toFloat()
        val labelY = center.y + (labelRadius * sin(angleInRadians)).toFloat()

        val style = TextStyle(fontSize = 12.sp, color = Color.Black)
        val textLayoutResult = textMeasurer.measure(labelText, style)
        drawScope.drawText(
          textMeasurer = textMeasurer,
          text = labelText,
          style = style,
          topLeft =
          Offset(
            labelX - textLayoutResult.size.width / 2,
            labelY - textLayoutResult.size.height / 2,
          ),
        )
      }

      startAngle += sweepAngle
    }
  }
}
