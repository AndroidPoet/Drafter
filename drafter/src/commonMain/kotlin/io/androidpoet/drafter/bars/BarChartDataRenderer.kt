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

import androidx.compose.ui.graphics.drawscope.DrawScope

public interface BarChartDataRenderer : io.androidpoet.drafter.core.ChartRenderer {
  /** The labels for the X-axis (one per bar group, bin, etc.). */
  public fun getLabels(): List<String>

  /** How many bars are in each group? (e.g. 1 for simple/histogram, >1 for grouped) */
  public fun barsPerGroup(): Int

  /** The maximum Y value to use for scaling the bars. */
  public fun calculateMaxValue(): Float

  /**
   * Given [chartWidth], [dataSize], and [barsPerGroup], return (barWidth, groupSpacing).
   * Must clamp or safeguard so values never go negative.
   */
  public fun calculateBarAndSpacing(
    chartWidth: Float,
    dataSize: Int,
    barsPerGroup: Int,
  ): Pair<Float, Float>

  /**
   * For layout, how wide is a single "group" on the X-axis? (barWidth * barsPerGroup + internal spacing).
   */
  public fun calculateGroupWidth(
    barWidth: Float,
    barsPerGroup: Int,
  ): Float

  /**
   * Draw the bars for group [index]. This is where grouped, stacked, or histogram logic goes.
   */
  public fun drawBars(
    drawScope: DrawScope,
    index: Int,
    left: Float,
    barWidth: Float,
    groupSpacing: Float,
    chartBottom: Float,
    chartHeight: Float,
    maxValue: Float,
    animationProgress: Float,
  )
}
