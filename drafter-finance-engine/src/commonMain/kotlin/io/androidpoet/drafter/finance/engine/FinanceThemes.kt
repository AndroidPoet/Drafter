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
package io.androidpoet.drafter.finance.engine

import io.androidpoet.drafter.finance.engine.scene.ChartColor

/**
 * Ready-made color palettes for every series, supplied as plain style factories.
 *
 * Two presets ship in the box; both live here, in the shared engine, so the
 * Compose and SwiftUI renderers draw byte-identical colors:
 *
 * - [DrafterTheme] — the default. A calm, premium palette (green / coral / indigo)
 *   with MA5/10/20 overlays on candles.
 * - [TradingViewTheme] — TradingView Lightweight Charts' exact default colors
 *   (up `#26a69a`, down `#ef5350`, line `#2196f3`, …) for a pixel-faithful look.
 *
 * Every factory just returns a `*SeriesStyle`, so callers keep full per-field
 * customization — pass a preset, then `.copy(...)` whatever you want to override.
 */
public interface FinanceTheme {
  public fun candle(withMovingAverages: Boolean = true): CandleStyle
  public fun bar(): BarSeriesStyle
  public fun line(): LineSeriesStyle
  public fun area(): AreaSeriesStyle
  public fun baseline(baseValue: Float): BaselineSeriesStyle
  public fun histogram(): HistogramSeriesStyle
  public fun volume(): VolumeStyle
}

/** The default Drafter palette — calm premium tones, no red. */
public object DrafterTheme : FinanceTheme {
  private val Up = ChartColor.rgba(0x49, 0xC1, 0x7A)
  private val Down = ChartColor.rgba(0xF2, 0x76, 0x6B)
  private val Ma5 = ChartColor.rgba(0xF6, 0xB2, 0x4C)
  private val Ma10 = ChartColor.rgba(0x4C, 0x8D, 0xF6)
  private val Ma20 = ChartColor.rgba(0x7C, 0x6B, 0xF2)
  private val Line = ChartColor.rgba(0x4C, 0x8D, 0xF6)
  private val Area = ChartColor.rgba(0x5B, 0x6B, 0xF0)
  private val Hist = ChartColor.rgba(0x2F, 0xC4, 0xC0)

  override fun candle(withMovingAverages: Boolean): CandleStyle =
    CandleStyle(
      up = Up,
      down = Down,
      movingAverages =
      if (withMovingAverages) {
        listOf(MaConfig(5, Ma5), MaConfig(10, Ma10), MaConfig(20, Ma20))
      } else {
        emptyList()
      },
    )

  override fun bar(): BarSeriesStyle = BarSeriesStyle(up = Up, down = Down)

  override fun line(): LineSeriesStyle = LineSeriesStyle(color = Line)

  override fun area(): AreaSeriesStyle =
    AreaSeriesStyle(
      lineColor = Area,
      fillColor = Area.copy(argb = (Area.argb and 0xFFFFFF) or (0x2EL shl 24)),
    )

  override fun baseline(baseValue: Float): BaselineSeriesStyle =
    BaselineSeriesStyle(
      baseValue = baseValue,
      topLineColor = Up,
      topFillColor = ChartColor.rgba(Up.red, Up.green, Up.blue, 0x26),
      bottomLineColor = Down,
      bottomFillColor = ChartColor.rgba(Down.red, Down.green, Down.blue, 0x26),
    )

  override fun histogram(): HistogramSeriesStyle = HistogramSeriesStyle(color = Hist)

  override fun volume(): VolumeStyle =
    VolumeStyle(
      up = ChartColor.rgba(Up.red, Up.green, Up.blue, 0xCC),
      down = ChartColor.rgba(Down.red, Down.green, Down.blue, 0xCC),
    )
}

/**
 * TradingView Lightweight Charts' exact default colors. Down candles are red
 * (`#ef5350`), matching TradingView pixel-for-pixel — opt in by passing this
 * theme. There are no MA overlays (Lightweight Charts has none by default).
 */
public object TradingViewTheme : FinanceTheme {
  // Lightweight Charts series defaults.
  private val Up = ChartColor.rgba(38, 166, 154) // #26a69a
  private val Down = ChartColor.rgba(239, 83, 80) // #ef5350
  private val Line = ChartColor.rgba(33, 150, 243) // #2196f3
  private val AreaLine = ChartColor.rgba(51, 215, 120) // #33D778
  private val AreaFill = ChartColor.rgba(46, 220, 135, 0x66) // rgba(46,220,135,0.4)

  override fun candle(withMovingAverages: Boolean): CandleStyle =
    CandleStyle(up = Up, down = Down)

  override fun bar(): BarSeriesStyle = BarSeriesStyle(up = Up, down = Down)

  override fun line(): LineSeriesStyle = LineSeriesStyle(color = Line, lineWidth = 3f)

  override fun area(): AreaSeriesStyle =
    AreaSeriesStyle(lineColor = AreaLine, fillColor = AreaFill, lineWidth = 3f)

  override fun baseline(baseValue: Float): BaselineSeriesStyle =
    BaselineSeriesStyle(
      baseValue = baseValue,
      topLineColor = Up,
      topFillColor = ChartColor.rgba(38, 166, 154, 0x47), // rgba(38,166,154,0.28)
      bottomLineColor = Down,
      bottomFillColor = ChartColor.rgba(239, 83, 80, 0x47), // rgba(239,83,80,0.28)
    )

  override fun histogram(): HistogramSeriesStyle = HistogramSeriesStyle(color = Up)

  override fun volume(): VolumeStyle =
    VolumeStyle(
      up = ChartColor.rgba(Up.red, Up.green, Up.blue, 0xCC),
      down = ChartColor.rgba(Down.red, Down.green, Down.blue, 0xCC),
    )
}
