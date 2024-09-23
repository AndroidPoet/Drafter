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
package io.androidpoet.drafter.radar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
public fun RadarChart(
  modifier: Modifier = Modifier,
  data: List<RadarChartData>,
  colors: List<Color>,
) {
  val textMeasurer = rememberTextMeasurer()

  // Animation
  val animatedProgress = remember { Animatable(0f) }

  LaunchedEffect(data) {
    animatedProgress.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )
  }

  Canvas(modifier = modifier.size(300.dp)) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.width.coerceAtMost(size.height) / 2 * 0.8f

    // Draw background circles and axes
    drawBackgroundAndAxes(centerX, centerY, radius, textMeasurer, data.first().values.keys.toList())

    // Draw data polygons with animation
    data.forEachIndexed { index, chartData ->
      drawDataPolygon(centerX, centerY, radius, chartData, colors[index], animatedProgress.value)
    }
  }
}

private fun DrawScope.drawBackgroundAndAxes(
  centerX: Float,
  centerY: Float,
  radius: Float,
  textMeasurer: TextMeasurer,
  axisLabels: List<String>,
) {
  val numberOfAxes = axisLabels.size

  // Draw background circles
  for (i in 1..10) {
    drawCircle(
      color = Color.LightGray,
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

    drawLine(
      color = Color.Gray,
      start = Offset(centerX, centerY),
      end = Offset(endX, endY),
      strokeWidth = 1f,
    )

    // Draw axis labels using TextMeasurer
    val textLayoutResult = textMeasurer.measure(
      text = axisLabels[i],
      style = TextStyle(
        color = Color.Black,
        fontSize = 12.sp,
      ),
    )

    drawText(
      textLayoutResult = textLayoutResult,
      topLeft = Offset(
        x = endX - textLayoutResult.size.width / 2,
        y = endY - textLayoutResult.size.height / 2,
      ),
    )
  }
}

private fun DrawScope.drawDataPolygon(
  centerX: Float,
  centerY: Float,
  radius: Float,
  data: RadarChartData,
  color: Color,
  progress: Float,
) {
  val points = data.values.values.mapIndexed { index, value ->
    val angle = index * 2 * PI / data.values.size - PI / 2
    val x = centerX + radius * value * progress * cos(angle).toFloat()
    val y = centerY + radius * value * progress * sin(angle).toFloat()
    Offset(x, y)
  }

  val path = Path().apply {
    moveTo(points.first().x, points.first().y)
    points.forEach { lineTo(it.x, it.y) }
    close()
  }

  drawPath(
    path = path,
    color = color.copy(alpha = 0.5f * progress),
    style = androidx.compose.ui.graphics.drawscope.Fill,
  )

  drawPath(
    path = path,
    color = color.copy(alpha = progress),
    style = Stroke(width = 2f),
  )
}
