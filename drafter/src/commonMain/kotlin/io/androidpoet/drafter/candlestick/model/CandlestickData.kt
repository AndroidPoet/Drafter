package io.androidpoet.drafter.candlestick.model

import androidx.compose.runtime.Immutable

@Immutable
public data class Candle(
  val label: String,
  val open: Float,
  val high: Float,
  val low: Float,
  val close: Float,
)

@Immutable
public data class CandlestickData(
  val candles: List<Candle>,
)
