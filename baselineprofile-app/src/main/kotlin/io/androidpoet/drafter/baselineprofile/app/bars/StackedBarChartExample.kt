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
package io.androidpoet.drafter.baselineprofile.app.bars
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.bars.BarChart
import io.androidpoet.drafter.bars.StackedBarChartData
import io.androidpoet.drafter.bars.StackedBarChartRenderer
import io.androidpoet.drafter.baselineprofile.app.ChartContainer
import io.androidpoet.drafter.baselineprofile.app.ChartTitle

@Composable
fun StackedBarChartExample() {
  ChartTitle(text = "Stacked Bar Chart")
  val data =
    StackedBarChartData(
      labels = listOf("Jan", "Feb", "Mar", "Apr"),
      stacks =
      listOf(
        listOf(5f, 5f, 2f),
        listOf(7f, 3f, 4f),
        listOf(6f, 4f, 3f),
        listOf(8f, 2f, 5f),
      ),
      colors = listOf(Color.Blue, Color.Red, Color.Green),
    )
  val renderer = StackedBarChartRenderer()
  ChartContainer {
    BarChart(
      data = data,
      renderer = renderer,
      modifier = Modifier.fillMaxSize(),
    )
  }
}
