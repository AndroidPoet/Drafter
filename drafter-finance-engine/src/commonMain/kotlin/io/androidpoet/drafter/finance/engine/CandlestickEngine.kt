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

import io.androidpoet.drafter.finance.engine.geometry.FPoint
import io.androidpoet.drafter.finance.engine.geometry.FRect
import io.androidpoet.drafter.finance.engine.indicator.Indicators
import io.androidpoet.drafter.finance.engine.model.Candle
import io.androidpoet.drafter.finance.engine.scale.LinearScale
import io.androidpoet.drafter.finance.engine.scene.ChartColor
import io.androidpoet.drafter.finance.engine.scene.DrawCommand
import io.androidpoet.drafter.finance.engine.scene.LineCmd
import io.androidpoet.drafter.finance.engine.scene.PolylineCmd
import io.androidpoet.drafter.finance.engine.scene.RectCmd
import io.androidpoet.drafter.finance.engine.scene.Scene
import kotlin.math.max
import kotlin.math.min

/** The inclusive range of candle indices currently visible (pan/zoom state). */
public data class CandleWindow(
  public val firstIndex: Int,
  public val lastIndex: Int,
) {
  public val count: Int get() = lastIndex - firstIndex + 1
}

/** A moving-average overlay request. */
public data class MaConfig(
  public val period: Int,
  public val color: ChartColor,
)

/** Visual configuration for the candlestick scene. */
public data class CandleStyle(
  public val up: ChartColor,
  public val down: ChartColor,
  public val wickWidth: Float = 1.5f,
  public val bodyWidthRatio: Float = 0.7f,
  public val movingAverages: List<MaConfig> = emptyList(),
)

/**
 * Turns candles + a visible [CandleWindow] + a plot [FRect] into a flat display
 * list. Contains all the geometry/scaling math; the renderer just draws what it
 * returns. Pure and deterministic — identical inputs always yield an identical
 * [Scene], which is what the golden fixtures assert.
 */
public object CandlestickEngine {
  public fun build(
    candles: List<Candle>,
    window: CandleWindow,
    plot: FRect,
    style: CandleStyle,
  ): Scene {
    val commands = ArrayList<DrawCommand>()
    if (candles.isEmpty() || window.count <= 0) return Scene(commands, plot)

    val first = window.firstIndex.coerceIn(0, candles.lastIndex)
    val last = window.lastIndex.coerceIn(first, candles.lastIndex)
    val visible = candles.subList(first, last + 1)

    var minLow = Float.MAX_VALUE
    var maxHigh = -Float.MAX_VALUE
    for (c in visible) {
      if (c.low < minLow) minLow = c.low
      if (c.high > maxHigh) maxHigh = c.high
    }
    // Max price -> top (smaller y); min price -> bottom.
    val priceScale = LinearScale(minLow, maxHigh, plot.bottom, plot.top)

    val n = visible.size
    val slot = plot.width / n
    val bodyWidth = (slot * style.bodyWidthRatio).coerceAtLeast(1f)

    fun centerX(i: Int): Float = plot.left + slot * i + slot / 2f

    visible.forEachIndexed { i, candle ->
      val cx = centerX(i)
      val up = candle.close >= candle.open
      val color = if (up) style.up else style.down

      // Wick.
      commands.add(
        LineCmd(
          x1 = cx,
          y1 = priceScale.toPixel(candle.high),
          x2 = cx,
          y2 = priceScale.toPixel(candle.low),
          color = color,
          strokeWidth = style.wickWidth,
        ),
      )

      // Body (open <-> close), with a 1px floor so doji candles stay visible.
      val topY = priceScale.toPixel(max(candle.open, candle.close))
      val bottomY = priceScale.toPixel(min(candle.open, candle.close))
      commands.add(
        RectCmd(
          rect = FRect(cx - bodyWidth / 2f, topY, cx + bodyWidth / 2f, max(bottomY, topY + 1f)),
          color = color,
          fill = true,
          cornerRadius = 1f,
        ),
      )
    }

    // Moving-average overlays on the close price.
    if (style.movingAverages.isNotEmpty()) {
      val closes = visible.map { it.close }
      for (ma in style.movingAverages) {
        val series = Indicators.sma(closes, ma.period)
        val points = ArrayList<FPoint>()
        series.forEachIndexed { i, value ->
          if (value != null) points.add(FPoint(centerX(i), priceScale.toPixel(value)))
        }
        if (points.size >= 2) commands.add(PolylineCmd(points, ma.color, strokeWidth = 2f))
      }
    }

    return Scene(commands, plot)
  }
}
