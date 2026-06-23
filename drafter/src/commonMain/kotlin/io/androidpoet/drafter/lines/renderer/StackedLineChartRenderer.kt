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
package io.androidpoet.drafter.lines.renderer

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import io.androidpoet.drafter.internal.areaGradient
import io.androidpoet.drafter.internal.smoothPath
import io.androidpoet.drafter.lines.LineChartDataRenderer
import io.androidpoet.drafter.lines.model.StackedLineChartData

@Immutable
public class StackedLineChartRenderer(
  private val data: StackedLineChartData,
) : LineChartDataRenderer {
  override fun getLabels(): List<String> = data.labels

  override fun calculateMaxValue(): Float = data.stacks.map { it.sum() }.maxOrNull() ?: 0f

  override fun drawLines(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  ) {
    val numPoints = data.labels.size
    if (numPoints < 2 || maxValue <= 0f) return
    val baseline = chartTop + chartHeight
    val xPositions =
      List(numPoints) { index ->
        chartLeft + index * (chartWidth / (numPoints - 1))
      }

    val stackCount = data.stacks[0].size

    // Cumulative top of each stack level: cumulative[k][i] = sum of stacks[i][0..k].
    val cumulative =
      Array(stackCount) { k ->
        FloatArray(numPoints) { i ->
          var sum = 0f
          for (s in 0..k) sum += data.stacks[i][s]
          sum
        }
      }

    // Draw back-to-front (top stack first) so each colour's visible band is the
    // gap between its level and the one below — smooth curves, soft gradients,
    // and no polygon seams between bands.
    for (stackIndex in stackCount - 1 downTo 0) {
      val color = data.colors.getOrElse(stackIndex) { Color.Gray }
      val topPoints =
        List(numPoints) { i ->
          val ratio = (cumulative[stackIndex][i] * animationProgress) / maxValue
          val y = baseline - ratio * chartHeight
          Offset(xPositions[i], y)
        }

      val curve = smoothPath(topPoints)
      val fillPath =
        Path().apply {
          addPath(curve)
          lineTo(topPoints.last().x, baseline)
          lineTo(topPoints.first().x, baseline)
          close()
        }

      drawScope.drawPath(
        path = fillPath,
        brush = areaGradient(color, topPoints.minOf { it.y }, baseline, topAlpha = 0.85f),
        style = Fill,
      )
      drawScope.drawPath(
        path = curve,
        color = color,
        style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round),
      )
    }
  }
}
