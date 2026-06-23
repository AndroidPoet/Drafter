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

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import io.androidpoet.drafter.pie.model.PieChartData

/**
 * A single interface for all pie/donut chart renderers.
 * - [data] stores the PieChartData internally.
 * - [drawChart] is called by [PieChart] composable to do the drawing.
 */
public interface PieChartDataRenderer : io.androidpoet.drafter.core.ChartRenderer {
  public val data: PieChartData

  /**
   * Draw the chart (pie, donut, etc.).
   * @param drawScope current [DrawScope] to use for drawing.
   * @param size The canvas size.
   * @param progress A float [0..1] that can be used for animation.
   */
  public fun drawChart(
    drawScope: DrawScope,
    size: Size,
    progress: Float,
    textMeasurer: TextMeasurer,
    isSystemInDarkTheme: Boolean,
  )
}
