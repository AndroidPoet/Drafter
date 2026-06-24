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
package io.androidpoet.drafter.finance.engine.crosshair

import io.androidpoet.drafter.finance.engine.CandleWindow
import io.androidpoet.drafter.finance.engine.geometry.FRect
import io.androidpoet.drafter.finance.engine.model.Candle

/** The candle a crosshair has snapped to, plus the x where its line should draw. */
public data class CrosshairResult(
  public val index: Int,
  public val snappedX: Float,
  public val candle: Candle,
)

/**
 * Resolves a pointer x-position to the nearest candle (magnet mode). Pure hit
 * testing — the same math each renderer feeds its native pointer events into.
 */
public object Crosshair {
  public fun resolve(
    pointerX: Float,
    candles: List<Candle>,
    window: CandleWindow,
    plot: FRect,
  ): CrosshairResult? {
    if (candles.isEmpty() || window.count <= 0) return null
    val first = window.firstIndex.coerceIn(0, candles.lastIndex)
    val last = window.lastIndex.coerceIn(first, candles.lastIndex)
    val n = last - first + 1
    val slot = plot.width / n
    if (slot <= 0f) return null
    val rel = ((pointerX - plot.left) / slot).toInt().coerceIn(0, n - 1)
    val index = first + rel
    val snappedX = plot.left + slot * rel + slot / 2f
    return CrosshairResult(index, snappedX, candles[index])
  }
}
