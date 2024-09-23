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
package io.androidpoet.drafterdemo.bars

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.bars.BarChart
import io.androidpoet.drafter.bars.WaterfallChartData
import io.androidpoet.drafter.bars.WaterfallChartRenderer
import io.androidpoet.drafterdemo.ChartContainer
import io.androidpoet.drafterdemo.ChartTitle

@Composable
fun WaterfallChartExample() {
  ChartTitle(text = "Waterfall Chart")
  val data = WaterfallChartData(
    labels = listOf("Q1", "Q2", "Q3", "Q4"),
    values = listOf(500f, -200f, 300f, -100f),
    colors = listOf(Color.Green, Color.Red, Color.Green, Color.Red),
    initialValue = 1000f,
  )
  val renderer = WaterfallChartRenderer()
  ChartContainer(modifier = Modifier.height(300.dp)) {
    BarChart(
      data = data,
      renderer = renderer,
      modifier = Modifier.fillMaxSize(),
    )
  }
}
