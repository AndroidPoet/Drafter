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
package io.androidpoet.drafter.baselineprofile.app.buble

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.baselineprofile.app.ChartTitle
import io.androidpoet.drafter.buble.BubbleChart
import io.androidpoet.drafter.buble.BubbleChartData
import io.androidpoet.drafter.buble.SimpleBubbleChartDataRenderer

private fun getBubbleChartData() = BubbleChartData(
  series = listOf(
    listOf(
      BubbleChartData.BubbleData(10f, 26f, 30f, Color.Blue),
      BubbleChartData.BubbleData(26f, 30f, 60f, Color.Blue),
      BubbleChartData.BubbleData(26f, 46f, 45f, Color.Blue),
    ),
    listOf(
      BubbleChartData.BubbleData(14f, 15f, 30f, Color.Green),
      BubbleChartData.BubbleData(22f, 36f, 45f, Color.Green),
      BubbleChartData.BubbleData(90f, 57f, 75f, Color.Green),
    ),
    listOf(
      BubbleChartData.BubbleData(8f, 9f, 90f, Color.Yellow),
      BubbleChartData.BubbleData(20f, 57f, 45f, Color.Yellow),
      BubbleChartData.BubbleData(40f, 50f, 60f, Color.Yellow),
    ),
    listOf(
      BubbleChartData.BubbleData(8f, 20f, 22.5f, Color.Red),
      BubbleChartData.BubbleData(12f, 30f, 30f, Color.Red),
      BubbleChartData.BubbleData(30f, 40f, 45f, Color.Red),
    ),
  ),
)

private fun getBubbleChartRenderer() = SimpleBubbleChartDataRenderer(getBubbleChartData())

@Composable
fun BubbleChartExample() {
  ChartTitle(text = "Bubble Chart")
  BubbleChart(
    renderer = getBubbleChartRenderer(),
    modifier = Modifier.size(300.dp),
  )
}
