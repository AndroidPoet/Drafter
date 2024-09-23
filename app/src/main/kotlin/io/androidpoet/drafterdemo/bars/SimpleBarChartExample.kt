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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.bars.BarChart
import io.androidpoet.drafter.bars.SimpleBarChartData
import io.androidpoet.drafter.bars.SimpleBarChartRenderer
import io.androidpoet.drafterdemo.ChartContainer
import io.androidpoet.drafterdemo.ChartTitle

@Composable
fun SimpleBarChartExample() {
  ChartTitle(text = "Simple Bar Chart")
  val data =
    SimpleBarChartData(
      labels = listOf("A", "B", "C", "D"),
      values = listOf(10f, 20f, 15f, 25f),
      colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow),
    )
  val renderer = SimpleBarChartRenderer()
  ChartContainer {
    BarChart(
      data = data,
      renderer = renderer,
      modifier = Modifier.fillMaxSize(),
    )
  }
}
