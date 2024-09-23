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
package io.androidpoet.drafter.buble

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
public fun BubbleChart(
  data: List<List<BubbleChartData>>,
  modifier: Modifier = Modifier,
) {
  val textMeasurer = rememberTextMeasurer()

  val animatedProgress = remember { Animatable(0f) } // Start at 0f

  // Launch the animation once when the composable enters the composition
  LaunchedEffect(Unit) {
    animatedProgress.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 2000),
    )
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val chartWidth = size.width - 60.dp.toPx()
    val chartHeight = size.height - 60.dp.toPx()
    val originX = 40.dp.toPx()
    val originY = size.height - 20.dp.toPx()

    drawGrid(chartWidth, chartHeight, originX, originY)
    drawAxes(chartWidth, chartHeight, originX, originY, textMeasurer)

    data.forEachIndexed { seriesIndex, series ->
      series.forEachIndexed { bubbleIndex, bubble ->
        val delay = (seriesIndex * series.size + bubbleIndex) * 0.1f
        val bubbleProgress = (animatedProgress.value - delay).coerceIn(0f, 1f)

        val x = originX + (bubble.x / 50f) * chartWidth
        val y = originY - (bubble.y / 70f) * chartHeight
        val animatedSize = bubble.size * bubbleProgress
        val animatedX = originX + (x - originX) * bubbleProgress
        val animatedY = originY - (originY - y) * bubbleProgress

        drawCircle(
          color = bubble.color,
          radius = animatedSize,
          center = Offset(animatedX, animatedY),
        )
      }
    }
  }
}

private fun DrawScope.drawGrid(
  chartWidth: Float,
  chartHeight: Float,
  originX: Float,
  originY: Float,
) {
  val gridColor = Color.LightGray

  // Vertical grid lines
  for (i in 0..5) {
    val x = originX + (i * chartWidth / 5f)
    drawLine(
      color = gridColor,
      start = Offset(x, originY),
      end = Offset(x, originY - chartHeight),
    )
  }

  // Horizontal grid lines
  for (i in 0..7) {
    val y = originY - (i * chartHeight / 7f)
    drawLine(
      color = gridColor,
      start = Offset(originX, y),
      end = Offset(originX + chartWidth, y),
    )
  }
}

private fun DrawScope.drawAxes(
  chartWidth: Float,
  chartHeight: Float,
  originX: Float,
  originY: Float,
  textMeasurer: TextMeasurer,
) {
  val axisColor = Color.Black
  val textStyle = TextStyle(color = Color.Black, fontSize = 10.sp)

  // X-axis
  drawLine(
    color = axisColor,
    start = Offset(originX, originY),
    end = Offset(originX + chartWidth, originY),
  )

  // Y-axis
  drawLine(
    color = axisColor,
    start = Offset(originX, originY),
    end = Offset(originX, originY - chartHeight),
  )

  // X-axis labels
  for (i in 0..5) {
    val x = originX + (i * chartWidth / 5f)
    val label = (i * 10).toString()
    val textLayoutResult = textMeasurer.measure(label, textStyle)
    drawText(
      textLayoutResult,
      topLeft = Offset(x - textLayoutResult.size.width / 2, originY + 5.dp.toPx()),
    )
  }

  // Y-axis labels
  for (i in 0..7) {
    val y = originY - (i * chartHeight / 7f)
    val label = (i * 10).toString()
    val textLayoutResult = textMeasurer.measure(label, textStyle)
    drawText(
      textLayoutResult,
      topLeft = Offset(
        originX - textLayoutResult.size.width - 5.dp.toPx(),
        y - textLayoutResult.size.height / 2,
      ),
    )
  }
}
