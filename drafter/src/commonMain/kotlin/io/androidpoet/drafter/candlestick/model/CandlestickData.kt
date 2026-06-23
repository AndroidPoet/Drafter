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
package io.androidpoet.drafter.candlestick.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
public data class Candle(
  val label: String,
  val open: Float,
  val high: Float,
  val low: Float,
  val close: Float,
)

/**
 * A simple moving-average overlay for a candlestick (K-line) chart.
 *
 * The average of the closing price over the trailing [period] candles is drawn
 * as a smooth line on top of the candles — the classic MA5 / MA10 / MA20 study.
 *
 * @param period number of trailing candles to average (e.g. 5, 10, 20).
 * @param color line color.
 */
@Immutable
public data class MovingAverage(
  val period: Int,
  val color: Color,
)

@Immutable
public data class CandlestickData(
  val candles: List<Candle>,
  val movingAverages: List<MovingAverage> = emptyList(),
)
