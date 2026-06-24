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
package io.androidpoet.drafter.finance.engine.indicator

import kotlin.math.sqrt

/** MACD line, signal line and histogram, each aligned to the input length. */
public data class MacdResult(
  public val macd: List<Float?>,
  public val signal: List<Float?>,
  public val histogram: List<Float?>,
)

/** Bollinger Band triple (middle = SMA), each aligned to the input length. */
public data class BollingerBands(
  public val middle: List<Float?>,
  public val upper: List<Float?>,
  public val lower: List<Float?>,
)

/**
 * Deterministic technical-indicator math — the canonical reference every SDK
 * port mirrors. Every function returns a list aligned 1:1 with the input, using
 * `null` for leading positions where the indicator is undefined. These exact
 * definitions are frozen by the golden fixtures in `drafter-finance-spec`.
 */
public object Indicators {
  /** Simple moving average over the trailing [period] samples. */
  public fun sma(values: List<Float>, period: Int): List<Float?> {
    require(period > 0) { "period must be > 0" }
    val out = ArrayList<Float?>(values.size)
    var sum = 0f
    for (i in values.indices) {
      sum += values[i]
      if (i >= period) sum -= values[i - period]
      out.add(if (i >= period - 1) sum / period else null)
    }
    return out
  }

  /**
   * Exponential moving average, seeded with the SMA of the first [period]
   * samples (the convention used by most charting tools), smoothing
   * `k = 2 / (period + 1)`.
   */
  public fun ema(values: List<Float>, period: Int): List<Float?> {
    require(period > 0) { "period must be > 0" }
    val out = ArrayList<Float?>(values.size)
    if (values.size < period) {
      repeat(values.size) { out.add(null) }
      return out
    }
    val k = 2f / (period + 1)
    var seed = 0f
    for (i in 0 until period) seed += values[i]
    seed /= period
    var prev = seed
    for (i in values.indices) {
      when {
        i < period - 1 -> out.add(null)
        i == period - 1 -> out.add(prev)
        else -> {
          prev = values[i] * k + prev * (1 - k)
          out.add(prev)
        }
      }
    }
    return out
  }

  /** Wilder's Relative Strength Index. Output is in `0..100`. */
  public fun rsi(values: List<Float>, period: Int = 14): List<Float?> {
    val out = ArrayList<Float?>(values.size)
    if (values.size < period + 1) {
      repeat(values.size) { out.add(null) }
      return out
    }
    out.add(null) // first sample has no prior change
    var avgGain = 0f
    var avgLoss = 0f
    for (i in 1 until values.size) {
      val change = values[i] - values[i - 1]
      val gain = if (change > 0f) change else 0f
      val loss = if (change < 0f) -change else 0f
      when {
        i < period -> {
          avgGain += gain
          avgLoss += loss
          out.add(null)
        }
        i == period -> {
          avgGain = (avgGain + gain) / period
          avgLoss = (avgLoss + loss) / period
          out.add(rsiFrom(avgGain, avgLoss))
        }
        else -> {
          avgGain = (avgGain * (period - 1) + gain) / period
          avgLoss = (avgLoss * (period - 1) + loss) / period
          out.add(rsiFrom(avgGain, avgLoss))
        }
      }
    }
    return out
  }

  private fun rsiFrom(avgGain: Float, avgLoss: Float): Float {
    if (avgLoss == 0f) return 100f
    val rs = avgGain / avgLoss
    return 100f - 100f / (1f + rs)
  }

  /** MACD = EMA(fast) - EMA(slow); signal = EMA(signalPeriod) of MACD. */
  public fun macd(
    values: List<Float>,
    fastPeriod: Int = 12,
    slowPeriod: Int = 26,
    signalPeriod: Int = 9,
  ): MacdResult {
    val fast = ema(values, fastPeriod)
    val slow = ema(values, slowPeriod)
    val macdLine = ArrayList<Float?>(values.size)
    for (i in values.indices) {
      val f = fast[i]
      val s = slow[i]
      macdLine.add(if (f != null && s != null) f - s else null)
    }
    // Signal = EMA of the defined portion of the MACD line, re-aligned.
    val defined = macdLine.filterNotNull()
    val signalDefined = ema(defined, signalPeriod)
    val offset = macdLine.indexOfFirst { it != null }
    val signal = ArrayList<Float?>(values.size)
    repeat(values.size) { signal.add(null) }
    if (offset >= 0) {
      for (j in signalDefined.indices) signal[offset + j] = signalDefined[j]
    }
    val histogram = ArrayList<Float?>(values.size)
    for (i in values.indices) {
      val m = macdLine[i]
      val sg = signal[i]
      histogram.add(if (m != null && sg != null) m - sg else null)
    }
    return MacdResult(macdLine, signal, histogram)
  }

  /** Bollinger Bands: middle = SMA(period); bands = middle ± mult·σ (population). */
  public fun bollinger(
    values: List<Float>,
    period: Int = 20,
    mult: Float = 2f,
  ): BollingerBands {
    require(period > 0) { "period must be > 0" }
    val middle = sma(values, period)
    val upper = ArrayList<Float?>(values.size)
    val lower = ArrayList<Float?>(values.size)
    for (i in values.indices) {
      val mid = middle[i]
      if (mid == null) {
        upper.add(null)
        lower.add(null)
        continue
      }
      var variance = 0f
      for (j in (i - period + 1)..i) {
        val d = values[j] - mid
        variance += d * d
      }
      val sd = sqrt(variance / period)
      upper.add(mid + mult * sd)
      lower.add(mid - mult * sd)
    }
    return BollingerBands(middle, upper, lower)
  }
}
