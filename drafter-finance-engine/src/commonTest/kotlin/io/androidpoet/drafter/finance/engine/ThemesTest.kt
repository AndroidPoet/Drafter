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

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Golden color values for both shipped themes. The SwiftUI port asserts the
 * IDENTICAL RGB in `ThemesTests.swift` — if these disagree, the palettes have
 * drifted across the two SDKs.
 */
class ThemesTest {
  @Test
  fun tradingViewMatchesLightweightChartsDefaults() {
    val up = TradingViewTheme.candle().up // #26a69a
    assertEquals(Triple(38, 166, 154), Triple(up.red, up.green, up.blue))
    val down = TradingViewTheme.candle().down // #ef5350
    assertEquals(Triple(239, 83, 80), Triple(down.red, down.green, down.blue))
    val line = TradingViewTheme.line() // #2196f3, width 3
    assertEquals(Triple(33, 150, 243), Triple(line.color.red, line.color.green, line.color.blue))
    assertEquals(3f, line.lineWidth)
  }

  @Test
  fun drafterDefaultHasNoMaOnTradingView() {
    // Drafter ships MA5/10/20; TradingView (like Lightweight Charts) ships none.
    assertEquals(3, DrafterTheme.candle().movingAverages.size)
    assertEquals(0, TradingViewTheme.candle().movingAverages.size)
  }

  @Test
  fun drafterDownIsCoralNotRed() {
    val down = DrafterTheme.candle().down // #F2766B coral, deliberately not red
    assertEquals(Triple(242, 118, 107), Triple(down.red, down.green, down.blue))
  }
}
