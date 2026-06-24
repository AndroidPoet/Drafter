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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.finance.engine.CandleStyle
import io.androidpoet.drafter.finance.engine.CandleWindow
import io.androidpoet.drafter.finance.engine.CandlestickEngine
import io.androidpoet.drafter.finance.engine.DrafterTheme
import io.androidpoet.drafter.finance.engine.crosshair.Crosshair
import io.androidpoet.drafter.finance.engine.geometry.FRect
import io.androidpoet.drafter.finance.engine.model.Candle
import io.androidpoet.drafter.finance.engine.scene.Scene
import kotlin.math.round

/** Default trading style — green/coral candles with MA5/MA10/MA20 overlays. */
public fun defaultCandleStyle(withMovingAverages: Boolean = true): CandleStyle =
  DrafterTheme.candle(withMovingAverages)

/**
 * An interactive candlestick / K-line chart. All geometry and indicator math
 * come from `:drafter-finance-engine`; this composable only draws the resulting
 * display list and overlays a scrub crosshair. The SwiftUI SDK mirrors it 1:1.
 *
 * @param candles OHLC series, oldest first.
 * @param showCrosshair draw a magnet crosshair + OHLC read-out while dragging.
 */
@Composable
public fun FinanceCandlestickChart(
  candles: List<Candle>,
  modifier: Modifier = Modifier,
  style: CandleStyle = defaultCandleStyle(),
  showCrosshair: Boolean = true,
) {
  val textMeasurer = rememberTextMeasurer()
  var cursor by remember { mutableStateOf<Offset?>(null) }

  Canvas(
    modifier = modifier
      .fillMaxSize()
      .pointerInput(candles) {
        detectDragGestures(
          onDragStart = { cursor = it },
          onDrag = { change, _ ->
            cursor = change.position
            change.consume()
          },
        )
      }
      .pointerInput(candles) {
        detectTapGestures { cursor = it }
      },
  ) {
    if (candles.isEmpty()) return@Canvas

    val plot = FRect(
      left = 8f,
      top = 8f,
      right = size.width - 56f,
      bottom = size.height - 8f,
    )
    val window = CandleWindow(0, candles.lastIndex)
    val scene: Scene = CandlestickEngine.build(candles, window, plot, style)
    drawScene(scene, textMeasurer)

    if (showCrosshair) {
      cursor?.let { c ->
        drawCrosshair(c, candles, window, plot, textMeasurer)
      }
    }
  }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCrosshair(
  cursor: Offset,
  candles: List<Candle>,
  window: CandleWindow,
  plot: FRect,
  textMeasurer: androidx.compose.ui.text.TextMeasurer,
) {
  val result = Crosshair.resolve(cursor.x, candles, window, plot) ?: return
  val lineColor = Color(0xFF8A92A2).copy(alpha = 0.55f)
  val clampedY = cursor.y.coerceIn(plot.top, plot.bottom)

  // Vertical (snapped to candle) + horizontal (free at cursor) crosshair lines.
  drawLine(
    lineColor,
    Offset(result.snappedX, plot.top),
    Offset(result.snappedX, plot.bottom),
    strokeWidth = 1f,
  )
  drawLine(lineColor, Offset(plot.left, clampedY), Offset(plot.right, clampedY), strokeWidth = 1f)

  // Price read-out at the right gutter, mapped from cursor y.
  val span = (plot.bottom - plot.top).let { if (it == 0f) 1f else it }
  val visible = candles.subList(
    window.firstIndex.coerceIn(0, candles.lastIndex),
    window.lastIndex.coerceIn(0, candles.lastIndex) + 1,
  )
  val minLow = visible.minOf { it.low }
  val maxHigh = visible.maxOf { it.high }
  val price = maxHigh - (clampedY - plot.top) / span * (maxHigh - minLow)
  val priceStyle = TextStyle(color = Color(0xFF1B1E25), fontSize = 10.sp)
  drawText(
    textMeasurer = textMeasurer,
    text = formatPrice(price),
    topLeft = Offset(plot.right + 4f, clampedY - 7f),
    style = priceStyle,
  )

  // OHLC read-out for the snapped candle, top-left.
  val c = result.candle
  val ohlc = "O ${formatPrice(
    c.open,
  )}  H ${formatPrice(c.high)}  L ${formatPrice(c.low)}  C ${formatPrice(c.close)}"
  drawText(
    textMeasurer = textMeasurer,
    text = ohlc,
    topLeft = Offset(plot.left + 2f, plot.top + 2f),
    style = TextStyle(color = Color(0xFF1B1E25), fontSize = 10.sp),
  )
}

private fun formatPrice(value: Float): String {
  val scaled = round(value * 100f).toInt()
  val whole = scaled / 100
  val frac = (if (scaled < 0) -scaled else scaled) % 100
  val fracStr = if (frac < 10) "0$frac" else "$frac"
  return "$whole.$fracStr"
}
