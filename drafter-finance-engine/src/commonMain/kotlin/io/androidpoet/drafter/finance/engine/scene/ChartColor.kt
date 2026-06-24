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
package io.androidpoet.drafter.finance.engine.scene

/**
 * A platform-agnostic color, stored as packed `0xAARRGGBB`.
 *
 * Renderers convert this to their native color type (Compose `Color`, SwiftUI
 * `Color`, etc.). Kept as a plain value so it serializes cleanly into goldens.
 */
public data class ChartColor(public val argb: Long) {
  public val alpha: Int get() = ((argb shr 24) and 0xFF).toInt()
  public val red: Int get() = ((argb shr 16) and 0xFF).toInt()
  public val green: Int get() = ((argb shr 8) and 0xFF).toInt()
  public val blue: Int get() = (argb and 0xFF).toInt()

  public companion object {
    public fun rgba(r: Int, g: Int, b: Int, a: Int = 255): ChartColor =
      ChartColor(
        ((a.toLong() and 0xFF) shl 24) or
          ((r.toLong() and 0xFF) shl 16) or
          ((g.toLong() and 0xFF) shl 8) or
          (b.toLong() and 0xFF),
      )
  }
}
