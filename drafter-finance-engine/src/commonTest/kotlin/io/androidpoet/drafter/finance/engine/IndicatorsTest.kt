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

import io.androidpoet.drafter.finance.engine.geometry.FRect
import io.androidpoet.drafter.finance.engine.indicator.Indicators
import io.androidpoet.drafter.finance.engine.model.Candle
import io.androidpoet.drafter.finance.engine.scene.ChartColor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Canonical golden values — the SwiftUI port asserts the IDENTICAL numbers in
 * `IndicatorsTests.swift`. If these two ever disagree, the SDKs have drifted.
 */
class IndicatorsTest {
  @Test
  fun sma() {
    val r = Indicators.sma(listOf(1f, 2f, 3f, 4f, 5f), 3)
    assertEquals(5, r.size)
    assertNull(r[0])
    assertNull(r[1])
    assertEquals(2f, r[2]!!, 0.0001f)
    assertEquals(3f, r[3]!!, 0.0001f)
    assertEquals(4f, r[4]!!, 0.0001f)
  }

  @Test
  fun emaSeededWithSma() {
    val r = Indicators.ema(listOf(1f, 2f, 3f, 4f, 5f), 3)
    assertNull(r[0])
    assertNull(r[1])
    assertEquals(2f, r[2]!!, 0.0001f)
    assertEquals(3f, r[3]!!, 0.0001f)
    assertEquals(4f, r[4]!!, 0.0001f)
  }

  @Test
  fun rsiInRange() {
    val values = listOf(1f, 2f, 3f, 4f, 5f, 4f, 3f, 4f, 5f, 6f, 7f, 6f, 5f, 6f, 7f, 8f)
    val r = Indicators.rsi(values, 14)
    assertEquals(values.size, r.size)
    for (v in r.filterNotNull()) {
      assertTrue(v in 0f..100f, "RSI out of range: $v")
    }
  }

  @Test
  fun candlestickSceneIsDeterministic() {
    val candles = listOf(
      Candle(0, 10f, 12f, 9f, 11f),
      Candle(1, 11f, 13f, 10f, 10f),
      Candle(2, 10f, 14f, 10f, 13f),
    )
    val scene = CandlestickEngine.build(
      candles = candles,
      window = CandleWindow(0, 2),
      plot = FRect(0f, 0f, 300f, 100f),
      style = CandleStyle(up = ChartColor.rgba(0, 255, 0), down = ChartColor.rgba(255, 0, 0)),
    )
    assertNotNull(scene)
    // 3 candles -> wick + body each = 6 commands.
    assertEquals(6, scene.commands.size)
  }
}
