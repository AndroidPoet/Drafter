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
package io.androidpoet.drafter.finance.engine.geometry

/** A 2D point in pixel space. Platform-agnostic — no Compose/UIKit types. */
public data class FPoint(
  public val x: Float,
  public val y: Float,
)

/** An axis-aligned rectangle in pixel space (y grows downward). */
public data class FRect(
  public val left: Float,
  public val top: Float,
  public val right: Float,
  public val bottom: Float,
) {
  public val width: Float get() = right - left
  public val height: Float get() = bottom - top
}
