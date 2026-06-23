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
package io.androidpoet.drafter.core

import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Formats a chart value deterministically on every target.
 *
 * [Float.toString] expands to long decimal strings on Kotlin/Wasm and Kotlin/JS
 * (e.g. `3.1f` renders as `"3.0999999046325684"`), which corrupts axis ticks and
 * value labels on the web demo. This builds the result from integer arithmetic
 * only, so it is exact everywhere — and cheap, so it will not thrash text
 * measurement while charts animate.
 *
 * Trailing zero decimals are dropped: `3.0 -> "3"`, `3.10 -> "3.1"`.
 *
 * @param value the number to format.
 * @param decimals maximum number of fractional digits to keep (default 1).
 */
internal fun formatChartValue(value: Float, decimals: Int = 1): String {
  if (value.isNaN() || value.isInfinite()) return "0"
  if (decimals <= 0) return value.roundToInt().toString()

  var factor = 1
  repeat(decimals) { factor *= 10 }

  val scaled = (abs(value) * factor).roundToInt()
  val intPart = scaled / factor
  var fracPart = scaled % factor
  var fracWidth = decimals
  while (fracPart != 0 && fracPart % 10 == 0) {
    fracPart /= 10
    fracWidth -= 1
  }

  val sign = if (value < 0f && scaled != 0) "-" else ""
  return if (fracPart == 0) {
    "$sign$intPart"
  } else {
    "$sign$intPart.${fracPart.toString().padStart(fracWidth, '0')}"
  }
}
