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
package io.androidpoet.drafter.scatterplot

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer

@Composable
public fun ScatterPlot(
  data: ScatterPlotData,
  renderer: ScatterPlotRenderer,
  modifier: Modifier = Modifier,
) {
  val textMeasurer = rememberTextMeasurer()
  val animationProgress = remember { Animatable(0f) }

  // Trigger the animation
  LaunchedEffect(Unit) {
    animationProgress.animateTo(
      targetValue = 1f,
      animationSpec =
      tween(
        durationMillis = 2000,
        easing = LinearOutSlowInEasing,
      ),
    )
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val chartHeight = size.height * 0.8f
    val chartWidth = size.width * 0.8f
    val chartTop = size.height * 0.1f
    val chartBottom = chartTop + chartHeight
    val chartLeft = size.width * 0.1f

    val (maxX, maxY) = renderer.calculateMaxValues(data)

    drawAxes(chartLeft, chartTop, chartBottom, chartWidth)
    drawYAxisLabels(textMeasurer, chartLeft, chartTop, chartBottom, maxY)
    drawXAxisLabels(textMeasurer, chartLeft, chartBottom, chartWidth, maxX)

    // Draw the points with animation
    renderer.drawPoints(
      drawScope = this,
      data = data,
      chartLeft = chartLeft,
      chartTop = chartTop,
      chartWidth = chartWidth,
      chartHeight = chartHeight,
      maxX = maxX,
      maxY = maxY,
      animationProgress = animationProgress.value, // Pass animation progress
    )
  }
}
