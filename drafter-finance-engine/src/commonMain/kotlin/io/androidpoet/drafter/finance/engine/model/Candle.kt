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
package io.androidpoet.drafter.finance.engine.model

/**
 * One OHLC(V) bar.
 *
 * @param time epoch value used only for ordering/labels (unit is caller-defined).
 * @param volume optional traded volume for the volume sub-pane; 0 if unused.
 */
public data class Candle(
  public val time: Long,
  public val open: Float,
  public val high: Float,
  public val low: Float,
  public val close: Float,
  public val volume: Float = 0f,
)
