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
import io.androidpoet.drafter.finance.engine.model.Candle
import io.androidpoet.drafter.finance.engine.scale.LinearScale
import io.androidpoet.drafter.finance.engine.scene.DrawCommand
import io.androidpoet.drafter.finance.engine.scene.FillPathCmd
import io.androidpoet.drafter.finance.engine.scene.LineCmd
import io.androidpoet.drafter.finance.engine.scene.PolylineCmd
import io.androidpoet.drafter.finance.engine.scene.RectCmd
import io.androidpoet.drafter.finance.engine.scene.Scene
import kotlin.math.max
import kotlin.math.min

/** Shared helpers for the value-based series. Kept internal — pure geometry. */
internal fun clampWindow(size: Int, window: CandleWindow): IntRange {
  if (size == 0) return IntRange.EMPTY
  val first = window.firstIndex.coerceIn(0, size - 1)
  val last = window.lastIndex.coerceIn(first, size - 1)
  return first..last
}

/** A value scale with a little headroom, mapping max -> top of the plot. */
internal fun valueScale(values: List<Float>, plot: FRect): LinearScale {
  var lo = Float.MAX_VALUE
  var hi = -Float.MAX_VALUE
  for (v in values) {
    if (v < lo) lo = v
    if (v > hi) hi = v
  }
  if (lo == Float.MAX_VALUE) {
    lo = 0f
    hi = 1f
  }
  if (lo == hi) {
    lo -= 1f
    hi += 1f
  }
  val pad = (hi - lo) * 0.08f
  return LinearScale(lo - pad, hi + pad, plot.bottom, plot.top)
}

private fun points(values: List<Float>, plot: FRect, scale: LinearScale): List<FPoint> {
  val n = values.size
  val slot = plot.width / n
  return values.mapIndexed { i, v -> FPoint(plot.left + slot * i + slot / 2f, scale.toPixel(v)) }
}

/** A simple line series. */
public object LineSeriesEngine {
  public fun build(
    values: List<Float>,
    window: CandleWindow,
    plot: FRect,
    style: LineSeriesStyle,
  ): Scene {
    val range = clampWindow(values.size, window)
    if (range.isEmpty()) return Scene(emptyList(), plot)
    val visible = values.subList(range.first, range.last + 1)
    val scale = valueScale(visible, plot)
    val pts = points(visible, plot, scale)
    val commands = if (pts.size >= 2) {
      listOf(
        PolylineCmd(pts, style.color, style.lineWidth),
      )
    } else {
      emptyList()
    }
    return Scene(commands, plot)
  }
}

/** A line with a filled area down to the plot baseline. */
public object AreaSeriesEngine {
  public fun build(
    values: List<Float>,
    window: CandleWindow,
    plot: FRect,
    style: AreaSeriesStyle,
  ): Scene {
    val range = clampWindow(values.size, window)
    if (range.isEmpty()) return Scene(emptyList(), plot)
    val visible = values.subList(range.first, range.last + 1)
    val scale = valueScale(visible, plot)
    val pts = points(visible, plot, scale)
    if (pts.size < 2) return Scene(emptyList(), plot)
    val fill = pts.toMutableList().apply {
      add(FPoint(pts.last().x, plot.bottom))
      add(FPoint(pts.first().x, plot.bottom))
    }
    return Scene(
      listOf(
        FillPathCmd(fill, style.fillColor),
        PolylineCmd(pts, style.lineColor, style.lineWidth),
      ),
      plot,
    )
  }
}

/** A baseline series: line + fill split above/below a [BaselineSeriesStyle.baseValue]. */
public object BaselineSeriesEngine {
  public fun build(
    values: List<Float>,
    window: CandleWindow,
    plot: FRect,
    style: BaselineSeriesStyle,
  ): Scene {
    val range = clampWindow(values.size, window)
    if (range.isEmpty()) return Scene(emptyList(), plot)
    val visible = values.subList(range.first, range.last + 1)
    val scale = valueScale(visible + style.baseValue, plot)
    val pts = points(visible, plot, scale)
    if (pts.size < 2) return Scene(emptyList(), plot)
    val baseY = scale.toPixel(style.baseValue)
    val commands = ArrayList<DrawCommand>()

    // Fill from the line down/up to the base line, colored by which side dominates.
    val topFill = pts.toMutableList().apply {
      add(FPoint(pts.last().x, baseY))
      add(FPoint(pts.first().x, baseY))
    }
    commands.add(FillPathCmd(topFill, style.topFillColor))

    // Base line.
    commands.add(LineCmd(plot.left, baseY, plot.right, baseY, style.bottomLineColor, 1f))

    // Value line, colored per segment by side of the base.
    for (i in 1 until pts.size) {
      val mid = (pts[i - 1].y + pts[i].y) / 2f
      val color = if (mid <= baseY) style.topLineColor else style.bottomLineColor
      commands.add(LineCmd(pts[i - 1].x, pts[i - 1].y, pts[i].x, pts[i].y, color, style.lineWidth))
    }
    return Scene(commands, plot)
  }
}

/** A histogram (bars from a base value). */
public object HistogramSeriesEngine {
  public fun build(
    values: List<Float>,
    window: CandleWindow,
    plot: FRect,
    style: HistogramSeriesStyle,
  ): Scene {
    val range = clampWindow(values.size, window)
    if (range.isEmpty()) return Scene(emptyList(), plot)
    val visible = values.subList(range.first, range.last + 1)
    val scale = valueScale(visible + style.baseValue, plot)
    val n = visible.size
    val slot = plot.width / n
    val barWidth = (slot * style.barWidthRatio).coerceAtLeast(1f)
    val baseY = scale.toPixel(style.baseValue)
    val commands = ArrayList<DrawCommand>()
    visible.forEachIndexed { i, v ->
      val cx = plot.left + slot * i + slot / 2f
      val y = scale.toPixel(v)
      commands.add(
        RectCmd(
          FRect(cx - barWidth / 2f, min(y, baseY), cx + barWidth / 2f, max(y, baseY)),
          style.color,
          fill = true,
        ),
      )
    }
    return Scene(commands, plot)
  }
}

/** A volume histogram colored by candle direction (up/down). */
public object VolumeEngine {
  public fun build(
    candles: List<Candle>,
    window: CandleWindow,
    plot: FRect,
    style: VolumeStyle,
  ): Scene {
    val range = clampWindow(candles.size, window)
    if (range.isEmpty()) return Scene(emptyList(), plot)
    val visible = candles.subList(range.first, range.last + 1)
    var maxVol = 0f
    for (c in visible) if (c.volume > maxVol) maxVol = c.volume
    if (maxVol <= 0f) maxVol = 1f
    val scale = LinearScale(0f, maxVol, plot.bottom, plot.top)
    val n = visible.size
    val slot = plot.width / n
    val barWidth = (slot * style.barWidthRatio).coerceAtLeast(1f)
    val commands = ArrayList<DrawCommand>()
    visible.forEachIndexed { i, c ->
      val cx = plot.left + slot * i + slot / 2f
      val color = if (c.close >= c.open) style.up else style.down
      commands.add(
        RectCmd(
          FRect(cx - barWidth / 2f, scale.toPixel(c.volume), cx + barWidth / 2f, plot.bottom),
          color,
          fill = true,
        ),
      )
    }
    return Scene(commands, plot)
  }
}

/** An OHLC bar series (American bars): a high-low stick with open/close ticks. */
public object BarSeriesEngine {
  public fun build(
    candles: List<Candle>,
    window: CandleWindow,
    plot: FRect,
    style: BarSeriesStyle,
  ): Scene {
    val range = clampWindow(candles.size, window)
    if (range.isEmpty()) return Scene(emptyList(), plot)
    val visible = candles.subList(range.first, range.last + 1)
    var minLow = Float.MAX_VALUE
    var maxHigh = -Float.MAX_VALUE
    for (c in visible) {
      if (c.low < minLow) minLow = c.low
      if (c.high > maxHigh) maxHigh = c.high
    }
    val scale = LinearScale(minLow, maxHigh, plot.bottom, plot.top)
    val n = visible.size
    val slot = plot.width / n
    val tick = (slot * style.tickRatio).coerceAtLeast(2f)
    val commands = ArrayList<DrawCommand>()
    visible.forEachIndexed { i, c ->
      val cx = plot.left + slot * i + slot / 2f
      val color = if (c.close >= c.open) style.up else style.down
      commands.add(
        LineCmd(cx, scale.toPixel(c.high), cx, scale.toPixel(c.low), color, style.thickness),
      )
      val openY = scale.toPixel(c.open)
      commands.add(LineCmd(cx - tick, openY, cx, openY, color, style.thickness))
      val closeY = scale.toPixel(c.close)
      commands.add(LineCmd(cx, closeY, cx + tick, closeY, color, style.thickness))
    }
    return Scene(commands, plot)
  }
}
