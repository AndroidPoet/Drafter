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
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

@Composable
public fun BubbleChart(
  renderer: BubbleChartDataRenderer,
  modifier: Modifier = Modifier,
  animate: Boolean = true,
) {
  val textMeasurer = rememberTextMeasurer()
  val animatedProgress = remember { Animatable(if (animate) 0f else 1f) }

  LaunchedEffect(animate) {
    if (animate) {
      animatedProgress.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 2000),
      )
    }
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val chartWidth = size.width - 60.dp.toPx()
    val chartHeight = size.height - 60.dp.toPx()
    val originX = 40.dp.toPx()
    val originY = size.height - 20.dp.toPx()

    drawGrid(chartWidth, chartHeight, originX, originY, renderer)
    drawAxes(chartWidth, chartHeight, originX, originY, textMeasurer, renderer)

    renderer.drawBubbles(
      drawScope = this,
      chartWidth = chartWidth,
      chartHeight = chartHeight,
      originX = originX,
      originY = originY,
      animationProgress = animatedProgress.value,
    )
  }
}

private fun DrawScope.drawGrid(
  chartWidth: Float,
  chartHeight: Float,
  originX: Float,
  originY: Float,
  renderer: BubbleChartDataRenderer,
) {
  val gridColor = Color.LightGray
  val ranges = renderer.getValueRanges()

  // Calculate steps for nice intervals
  val xRange = ranges.xMax - ranges.xMin
  val yRange = ranges.yMax - ranges.yMin

  val xStep = calculateGridStep(xRange)
  val yStep = calculateGridStep(yRange)

  // Calculate number of lines needed
  val xGridLines = (xRange / xStep).toInt()
  val yGridLines = (yRange / yStep).toInt()

  // Vertical grid lines
  for (i in 0..xGridLines) {
    val value = ranges.xMin + (i * xStep)
    val ratio = (value - ranges.xMin) / xRange
    val x = originX + (ratio * chartWidth)
    drawLine(
      color = gridColor,
      start = Offset(x, originY),
      end = Offset(x, originY - chartHeight),
    )
  }

  // Horizontal grid lines
  for (i in 0..yGridLines) {
    val value = ranges.yMin + (i * yStep)
    val ratio = (value - ranges.yMin) / yRange
    val y = originY - (ratio * chartHeight)
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
  renderer: BubbleChartDataRenderer,
) {
  val axisColor = Color.Black
  val textStyle = TextStyle(color = Color.Black, fontSize = 10.sp)
  val ranges = renderer.getValueRanges()

  // Draw axes lines
  drawLine(
    color = axisColor,
    start = Offset(originX, originY),
    end = Offset(originX + chartWidth, originY),
  )
  drawLine(
    color = axisColor,
    start = Offset(originX, originY),
    end = Offset(originX, originY - chartHeight),
  )

  // Calculate steps for nice intervals
  val xRange = ranges.xMax - ranges.xMin
  val yRange = ranges.yMax - ranges.yMin

  val xStep = calculateGridStep(xRange)
  val yStep = calculateGridStep(yRange)

  val xLabels = (xRange / xStep).toInt()
  val yLabels = (yRange / yStep).toInt()

  // X-axis labels
  for (i in 0..xLabels) {
    val value = ranges.xMin + (i * xStep)
    val ratio = (value - ranges.xMin) / xRange
    val x = originX + (ratio * chartWidth)
    val label = value.toInt().toString()

    val textLayoutResult = textMeasurer.measure(label, textStyle)
    drawText(
      textLayoutResult,
      topLeft = Offset(x - textLayoutResult.size.width / 2, originY + 5.dp.toPx()),
    )
  }

  // Y-axis labels
  for (i in 0..yLabels) {
    val value = ranges.yMin + (i * yStep)
    val ratio = (value - ranges.yMin) / yRange
    val y = originY - (ratio * chartHeight)
    val label = value.toInt().toString()

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

private fun calculateGridStep(maxValue: Float): Float {
  // Calculate a nice step size based on the max value
  val magnitude = floor(log10(maxValue.toDouble())).toFloat()
  val baseStep = 10.0f.pow(magnitude)

  return when {
    maxValue / baseStep > 5 -> baseStep * 2
    maxValue / baseStep > 2 -> baseStep
    else -> baseStep / 2
  }
}
