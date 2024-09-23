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
package io.androidpoet.drafterdemo.line
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.lines.GroupedLineChartData
import io.androidpoet.drafter.lines.GroupedLineChartRenderer
import io.androidpoet.drafter.lines.LineChart
import io.androidpoet.drafterdemo.ChartContainer
import io.androidpoet.drafterdemo.ChartTitle

@Composable
fun GroupedLineChartExample() {
  ChartTitle(text = "Grouped Line Chart")
  val data =
    GroupedLineChartData(
      labels = listOf("Q1", "Q2", "Q3", "Q4"),
      itemNames = listOf("Product A", "Product B"),
      groupedValues =
      listOf(
        listOf(10f, 15f),
        listOf(20f, 25f),
        listOf(15f, 10f),
        listOf(25f, 20f),
      ),
      colors = listOf(Color.Cyan, Color.Magenta),
    )
  val renderer = GroupedLineChartRenderer()
  ChartContainer {
    LineChart(
      data = data,
      renderer = renderer,
      modifier = Modifier.fillMaxSize(),
    )
  }
}
