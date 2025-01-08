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
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.pie.model.PieChartData
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

public class PieChartRenderer(
  override val data: PieChartData,
  private val labelThreshold: Float = 5f,
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
    val radius = size.minDimension / 2
    val center = Offset(size.width / 2, size.height / 2)

    data.slices.forEach { slice ->
      val slicePercentage = slice.value / totalValue
      val sweepAngle = slicePercentage * 360f * progress

      // Draw the arc
      drawScope.drawArc(
        color = slice.color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        topLeft = Offset.Zero,
        size = size,
      )

      // Optionally draw label if slice is >= threshold
      val percentage = slicePercentage * 100
      if (percentage >= labelThreshold && sweepAngle > 0f) {
        val angleMid = startAngle + sweepAngle / 2
        val angleRad = angleMid * (PI / 180)

        // 70% of the radius to place the label
        val labelRadius = radius * 0.7f
        val labelX = center.x + (labelRadius * cos(angleRad)).toFloat()
        val labelY = center.y + (labelRadius * sin(angleRad)).toFloat()

        val labelText = "${percentage.toInt()}%"
        val style = TextStyle(fontSize = 12.sp, color = Color.Black)
        val textLayout = textMeasurer.measure(labelText, style)

        drawScope.drawText(
          textMeasurer = textMeasurer,
          text = labelText,
          style = style,
          topLeft =
            Offset(
              x = labelX - textLayout.size.width / 2,
              y = labelY - textLayout.size.height / 2,
            ),
        )
      }

      startAngle += sweepAngle
    }
  }
}
