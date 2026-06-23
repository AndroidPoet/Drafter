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
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.androidpoet.drafter.internal.drawSmoothLine
import io.androidpoet.drafter.lines.LineChartDataRenderer
import io.androidpoet.drafter.lines.model.SimpleLineChartData

@Immutable
public class LineChartRenderer(
  private val data: SimpleLineChartData,
) : LineChartDataRenderer {
  override fun getLabels(): List<String> = data.labels

  override fun calculateMaxValue(): Float = data.values.maxOrNull() ?: 0f

  override fun drawLines(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  ) {
    if (data.values.size < 2 || maxValue <= 0f) return
    val baseline = chartTop + chartHeight
    val points =
      data.values.mapIndexed { index, value ->
        val x = chartLeft + index * (chartWidth / (data.values.size - 1))
        val y = baseline - (value / maxValue) * chartHeight
        Offset(x, y)
      }

    drawScope.drawSmoothLine(
      points = points,
      color = data.color,
      baseline = baseline,
      progress = animationProgress,
      strokeWidth = 6f,
      fill = true,
      endDot = true,
    )
  }
}
