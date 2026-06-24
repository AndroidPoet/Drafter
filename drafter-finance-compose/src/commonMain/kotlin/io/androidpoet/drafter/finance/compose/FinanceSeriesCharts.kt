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
package io.androidpoet.drafter.finance.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import io.androidpoet.drafter.finance.engine.AreaSeriesEngine
import io.androidpoet.drafter.finance.engine.AreaSeriesStyle
import io.androidpoet.drafter.finance.engine.BarSeriesEngine
import io.androidpoet.drafter.finance.engine.BarSeriesStyle
import io.androidpoet.drafter.finance.engine.BaselineSeriesEngine
import io.androidpoet.drafter.finance.engine.BaselineSeriesStyle
import io.androidpoet.drafter.finance.engine.CandleWindow
import io.androidpoet.drafter.finance.engine.HistogramSeriesEngine
import io.androidpoet.drafter.finance.engine.HistogramSeriesStyle
import io.androidpoet.drafter.finance.engine.LineSeriesEngine
import io.androidpoet.drafter.finance.engine.LineSeriesStyle
import io.androidpoet.drafter.finance.engine.VolumeEngine
import io.androidpoet.drafter.finance.engine.VolumeStyle
import io.androidpoet.drafter.finance.engine.geometry.FRect
import io.androidpoet.drafter.finance.engine.model.Candle
import io.androidpoet.drafter.finance.engine.scene.ChartColor

private fun plotOf(width: Float, height: Float): FRect =
  FRect(left = 8f, top = 8f, right = width - 8f, bottom = height - 8f)

/** A line series. */
@Composable
public fun FinanceLineChart(
  values: List<Float>,
  modifier: Modifier = Modifier,
  style: LineSeriesStyle = LineSeriesStyle(color = ChartColor.rgba(0x4C, 0x8D, 0xF6)),
) {
  val textMeasurer = rememberTextMeasurer()
  Canvas(modifier.fillMaxSize()) {
    if (values.isEmpty()) return@Canvas
    val plot = plotOf(size.width, size.height)
    val scene = LineSeriesEngine.build(values, CandleWindow(0, values.lastIndex), plot, style)
    drawScene(scene, textMeasurer)
  }
}

/** A line + filled area series. */
@Composable
public fun FinanceAreaChart(
  values: List<Float>,
  modifier: Modifier = Modifier,
  style: AreaSeriesStyle = AreaSeriesStyle(
    lineColor = ChartColor.rgba(0x5B, 0x6B, 0xF0),
    fillColor = ChartColor.rgba(0x5B, 0x6B, 0xF0, 0x2E),
  ),
) {
  val textMeasurer = rememberTextMeasurer()
  Canvas(modifier.fillMaxSize()) {
    if (values.isEmpty()) return@Canvas
    val plot = plotOf(size.width, size.height)
    val scene = AreaSeriesEngine.build(values, CandleWindow(0, values.lastIndex), plot, style)
    drawScene(scene, textMeasurer)
  }
}

/** A baseline series split above/below [BaselineSeriesStyle.baseValue]. */
@Composable
public fun FinanceBaselineChart(
  values: List<Float>,
  baseValue: Float,
  modifier: Modifier = Modifier,
  style: BaselineSeriesStyle = BaselineSeriesStyle(
    baseValue = baseValue,
    topLineColor = ChartColor.rgba(0x49, 0xC1, 0x7A),
    topFillColor = ChartColor.rgba(0x49, 0xC1, 0x7A, 0x26),
    bottomLineColor = ChartColor.rgba(0xF2, 0x76, 0x6B),
    bottomFillColor = ChartColor.rgba(0xF2, 0x76, 0x6B, 0x26),
  ),
) {
  val textMeasurer = rememberTextMeasurer()
  Canvas(modifier.fillMaxSize()) {
    if (values.isEmpty()) return@Canvas
    val plot = plotOf(size.width, size.height)
    val scene = BaselineSeriesEngine.build(values, CandleWindow(0, values.lastIndex), plot, style)
    drawScene(scene, textMeasurer)
  }
}

/** A histogram series. */
@Composable
public fun FinanceHistogramChart(
  values: List<Float>,
  modifier: Modifier = Modifier,
  style: HistogramSeriesStyle = HistogramSeriesStyle(color = ChartColor.rgba(0x2F, 0xC4, 0xC0)),
) {
  val textMeasurer = rememberTextMeasurer()
  Canvas(modifier.fillMaxSize()) {
    if (values.isEmpty()) return@Canvas
    val plot = plotOf(size.width, size.height)
    val scene = HistogramSeriesEngine.build(values, CandleWindow(0, values.lastIndex), plot, style)
    drawScene(scene, textMeasurer)
  }
}

/** An OHLC bar (American bar) series. */
@Composable
public fun FinanceBarChart(
  candles: List<Candle>,
  modifier: Modifier = Modifier,
  style: BarSeriesStyle = BarSeriesStyle(
    up = ChartColor.rgba(0x49, 0xC1, 0x7A),
    down = ChartColor.rgba(0xF2, 0x76, 0x6B),
  ),
) {
  val textMeasurer = rememberTextMeasurer()
  Canvas(modifier.fillMaxSize()) {
    if (candles.isEmpty()) return@Canvas
    val plot = plotOf(size.width, size.height)
    val scene = BarSeriesEngine.build(candles, CandleWindow(0, candles.lastIndex), plot, style)
    drawScene(scene, textMeasurer)
  }
}

/** A volume histogram colored by candle direction. */
@Composable
public fun FinanceVolumeChart(
  candles: List<Candle>,
  modifier: Modifier = Modifier,
  style: VolumeStyle = VolumeStyle(
    up = ChartColor.rgba(0x49, 0xC1, 0x7A, 0xCC),
    down = ChartColor.rgba(0xF2, 0x76, 0x6B, 0xCC),
  ),
) {
  val textMeasurer = rememberTextMeasurer()
  Canvas(modifier.fillMaxSize()) {
    if (candles.isEmpty()) return@Canvas
    val plot = plotOf(size.width, size.height)
    val scene = VolumeEngine.build(candles, CandleWindow(0, candles.lastIndex), plot, style)
    drawScene(scene, textMeasurer)
  }
}
