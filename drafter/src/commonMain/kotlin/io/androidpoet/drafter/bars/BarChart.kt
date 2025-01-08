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
package io.androidpoet.drafter.bars

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.popup.HoverState

/**
 * A customizable animated bar chart component for Jetpack Compose.
 *
 * @param renderer The data renderer that provides chart data and drawing logic
 * @param modifier Modifier for customizing the chart's layout
 * @param animate Whether to animate the chart on initial display (default: true)
 */
@Composable
public fun BarChart(
  renderer: BarChartDataRenderer,
  modifier: Modifier = Modifier,
  animate: Boolean = true,
) {
  // Remember text measurer for consistent text measurements
  val textMeasurer = rememberTextMeasurer()

  // Animation progress controller
  val animationProgress = remember {
    Animatable(if (animate) 0f else 1f)
  }
  var hoverState by remember { mutableStateOf(HoverState()) }
  val interactionSource = remember { MutableInteractionSource() }

  // Handle animation when component is first displayed
  LaunchedEffect(animate) {
    if (animate) {
      animationProgress.animateTo(
        targetValue = 1f,
        animationSpec = tween(
          durationMillis = 1000,
          easing = LinearOutSlowInEasing,
        ),
      )
    }
  }

  val labels = renderer.getLabels()
  val barsPerGroup = renderer.barsPerGroup()

  val scrollState = rememberScrollState()

  Canvas(
    modifier = modifier
      .fillMaxSize()
      .hoverable(interactionSource),
  ) {
    // Skip drawing if canvas size is invalid
    if (size.width < 1f || size.height < 1f) return@Canvas

    // Calculate chart dimensions and padding
    val chartDimensions = calculateChartDimensions(size.width, size.height)
    val (chartPadding, chartHeight, chartWidth, chartTop, chartBottom, chartLeft) = chartDimensions

    // Draw the basic chart structure
    drawAxes(chartLeft, chartTop, chartBottom, chartWidth)

    // Calculate bar dimensions based on data
    val maxValue = renderer.calculateMaxValue()
    val (barWidth, groupSpacing) = calculateBarDimensions(
      chartWidth = chartWidth,
      dataSize = labels.size,
      barsPerGroup = barsPerGroup,
    )

    // Draw axis labels and grid lines
    drawYAxisLabels(
      textMeasurer = textMeasurer,
      maxValue = maxValue,
      left = chartLeft,
      top = chartTop,
      bottom = chartBottom,
    )

    // Draw the actual bars
    drawBars(
      renderer = renderer,
      labels = labels,
      chartLeft = chartLeft,
      chartBottom = chartBottom,
      chartHeight = chartHeight,
      barWidth = barWidth,
      groupSpacing = groupSpacing,
      barsPerGroup = barsPerGroup,
      maxValue = maxValue,
      animationProgress = animationProgress.value,
    )

    // Draw x-axis labels
    drawXAxisLabels(
      textMeasurer = textMeasurer,
      labels = labels,
      chartLeft = chartLeft,
      chartBottom = chartBottom,
      barWidth = barWidth,
      barsPerGroup = barsPerGroup,
      groupSpacing = groupSpacing,
    )
  }
}

/**
 * Calculates the dimensions for the chart layout.
 * @return Triple of (padding, height, width, top, bottom, left)
 */
private fun calculateChartDimensions(width: Float, height: Float): ChartDimensions {
  val padding = width * 0.15f
  return ChartDimensions(
    chartPadding = padding,
    chartHeight = height * 0.7f,
    chartWidth = width - (padding * 2),
    chartTop = height * 0.15f,
    chartBottom = height * 0.15f + (height * 0.7f),
    chartLeft = padding,
  )
}

/**
 * Calculates the width of bars and spacing between groups.
 * @return Pair of (barWidth, groupSpacing)
 */
private fun calculateBarDimensions(
  chartWidth: Float,
  dataSize: Int,
  barsPerGroup: Int,
): Pair<Float, Float> {
  if (dataSize <= 0) return Pair(0f, 0f)

  val totalGroups = dataSize
  val totalSpacing = chartWidth * 0.2f
  val spacing = totalSpacing / (totalGroups + 1)
  val availableWidth = chartWidth - totalSpacing
  val barWidth = (availableWidth / (totalGroups * barsPerGroup))

  return Pair(barWidth, spacing)
}

/**
 * Draws the Y-axis labels with proper scaling and formatting.
 */
private fun DrawScope.drawYAxisLabels(
  textMeasurer: TextMeasurer,
  maxValue: Float,
  left: Float,
  top: Float,
  bottom: Float,
) {
  val style = TextStyle(fontSize = 12.sp, color = Color.Black)
  val steps = 5
  val stepValue = maxValue / steps

  for (i in 0..steps) {
    val value = stepValue * i
    val yPosition = bottom - ((value / maxValue) * (bottom - top))
    val roundedValue = (value * 10).toInt() / 10f
    val label = roundedValue.toString()

    val measured = textMeasurer.measure(label, style)
    drawText(
      textMeasurer = textMeasurer,
      text = label,
      style = style,
      topLeft = Offset(
        x = (left - measured.size.width - 8f),
        y = yPosition - (measured.size.height / 2),
      ),
    )
  }
}

/**
 * Draws the bars for each data point with proper spacing and animation.
 */
private fun DrawScope.drawBars(
  renderer: BarChartDataRenderer,
  labels: List<String>,
  chartLeft: Float,
  chartBottom: Float,
  chartHeight: Float,
  barWidth: Float,
  groupSpacing: Float,
  barsPerGroup: Int,
  maxValue: Float,
  animationProgress: Float,
) {
  var currentLeft = chartLeft
  labels.forEachIndexed { index, _ ->
    renderer.drawBars(
      drawScope = this,
      index = index,
      left = currentLeft,
      barWidth = barWidth,
      groupSpacing = groupSpacing,
      chartBottom = chartBottom,
      chartHeight = chartHeight,
      maxValue = maxValue,
      animationProgress = animationProgress,
    )

    val groupWidth = barWidth * barsPerGroup
    currentLeft += groupWidth + groupSpacing
  }
}

/**
 * Draws the X-axis labels centered under each bar group.
 */
private fun DrawScope.drawXAxisLabels(
  textMeasurer: TextMeasurer,
  labels: List<String>,
  chartLeft: Float,
  chartBottom: Float,
  barWidth: Float,
  barsPerGroup: Int,
  groupSpacing: Float,
) {
  val style = TextStyle(fontSize = 12.sp, color = Color.Black)
  var currentLeft = chartLeft

  labels.forEach { label ->
    val measured = textMeasurer.measure(label, style)
    val groupWidth = barWidth * barsPerGroup
    val centerX = currentLeft + (groupWidth / 2)

    drawText(
      textMeasurer = textMeasurer,
      text = label,
      style = style,
      topLeft = Offset(
        x = centerX - (measured.size.width / 2),
        y = chartBottom + 8f,
      ),
    )

    currentLeft += groupWidth + groupSpacing
  }
}

/**
 * Draws the basic axes of the chart.
 */
private fun DrawScope.drawAxes(
  left: Float,
  top: Float,
  bottom: Float,
  width: Float,
) {
  // Y-axis
  drawLine(
    color = Color.Black,
    start = Offset(left, top),
    end = Offset(left, bottom),
    strokeWidth = 2f,
  )
  // X-axis
  drawLine(
    color = Color.Black,
    start = Offset(left, bottom),
    end = Offset(left + width, bottom),
    strokeWidth = 2f,
  )
}

/**
 * Data class to hold chart dimension calculations
 */
private data class ChartDimensions(
  val chartPadding: Float,
  val chartHeight: Float,
  val chartWidth: Float,
  val chartTop: Float,
  val chartBottom: Float,
  val chartLeft: Float,
)
