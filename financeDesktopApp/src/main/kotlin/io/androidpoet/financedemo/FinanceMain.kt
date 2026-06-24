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
package io.androidpoet.financedemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.androidpoet.drafter.finance.compose.FinanceCandlestickChart
import io.androidpoet.drafter.finance.engine.model.Candle

fun main() = application {
  Window(
    onCloseRequest = ::exitApplication,
    state = rememberWindowState(width = 1100.dp, height = 720.dp),
    title = "Drafter Finance — Native K-Line (no WebView)",
  ) {
    FinanceDashboard()
  }
}

@Composable
private fun FinanceDashboard() {
  Box(
    Modifier
      .fillMaxSize()
      .background(Color(0xFFF4F6FA))
      .padding(20.dp),
  ) {
    Column(Modifier.fillMaxSize()) {
      BasicText(
        text = "BTC / USDT · 1D",
        style = TextStyle(fontSize = 18.sp, color = Color(0xFF1B1E25)),
      )
      Spacer(Modifier.height(2.dp))
      BasicText(
        text = "Native Compose · engine-driven · MA5/10/20 · drag to scrub the crosshair",
        style = TextStyle(fontSize = 12.sp, color = Color(0xFF8A92A2)),
      )
      Spacer(Modifier.height(14.dp))
      Box(
        Modifier
          .fillMaxSize()
          .background(Color.White, RoundedCornerShape(16.dp))
          .padding(10.dp),
      ) {
        FinanceCandlestickChart(
          candles = demoCandles(),
          modifier = Modifier.fillMaxSize(),
        )
      }
    }
  }
}

/** A deterministic ~60-bar OHLC walk so the chart looks like a real instrument. */
private fun demoCandles(): List<Candle> {
  val closes = listOf(
    100f, 102f, 101f, 104f, 108f, 106f, 109f, 113f, 111f, 110f,
    114f, 118f, 116f, 115f, 119f, 123f, 121f, 124f, 128f, 126f,
    125f, 122f, 124f, 121f, 119f, 122f, 126f, 124f, 127f, 131f,
    129f, 132f, 136f, 134f, 133f, 137f, 141f, 139f, 138f, 135f,
    137f, 134f, 132f, 135f, 139f, 142f, 140f, 143f, 147f, 145f,
    148f, 152f, 150f, 149f, 153f, 157f, 155f, 158f, 162f, 160f,
  )
  var prevClose = 99f
  return closes.mapIndexed { i, close ->
    val open = prevClose
    val swing = if (i % 3 == 0) 3f else 2f
    val high = maxOf(open, close) + swing
    val low = minOf(open, close) - swing
    val volume = 100f + (i % 7) * 25f
    prevClose = close
    Candle(time = i.toLong(), open = open, high = high, low = low, close = close, volume = volume)
  }
}
