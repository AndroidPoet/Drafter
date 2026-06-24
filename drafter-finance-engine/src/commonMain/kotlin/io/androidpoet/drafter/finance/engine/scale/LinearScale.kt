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
package io.androidpoet.drafter.finance.engine.scale

/**
 * Maps a value domain `[domainMin, domainMax]` onto a pixel range
 * `[rangeStart, rangeEnd]` linearly.
 *
 * For a price axis pass `rangeStart = bottomPx` and `rangeEnd = topPx` so that
 * the maximum price maps to the top of the plot (smaller y).
 */
public class LinearScale(
  public val domainMin: Float,
  public val domainMax: Float,
  public val rangeStart: Float,
  public val rangeEnd: Float,
) {
  private val domainSpan: Float = (domainMax - domainMin).let { if (it == 0f) 1f else it }

  public fun toPixel(value: Float): Float {
    val t = (value - domainMin) / domainSpan
    return rangeStart + t * (rangeEnd - rangeStart)
  }

  public fun toValue(pixel: Float): Float {
    val rangeSpan = (rangeEnd - rangeStart).let { if (it == 0f) 1f else it }
    val t = (pixel - rangeStart) / rangeSpan
    return domainMin + t * domainSpan
  }
}
