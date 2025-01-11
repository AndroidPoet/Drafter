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
import io.androidpoet.drafter.buble.BubbleChart
import io.androidpoet.drafter.buble.BubbleChartData
import io.androidpoet.drafter.buble.SimpleBubbleChartDataRenderer

private fun getBubbleChartData(colors: List<Color>) =
  BubbleChartData(
    series =
    listOf(
      listOf(
        BubbleChartData.BubbleData(10f, 26f, 30f, colors[0]),
        BubbleChartData.BubbleData(26f, 30f, 60f, colors[0]),
        BubbleChartData.BubbleData(26f, 46f, 45f, colors[0]),
      ),
      listOf(
        BubbleChartData.BubbleData(14f, 15f, 30f, colors[1]),
        BubbleChartData.BubbleData(22f, 36f, 45f, colors[1]),
        BubbleChartData.BubbleData(90f, 57f, 75f, colors[1]),
      ),
      listOf(
        BubbleChartData.BubbleData(8f, 9f, 90f, colors[2]),
        BubbleChartData.BubbleData(20f, 57f, 45f, colors[2]),
        BubbleChartData.BubbleData(40f, 50f, 60f, colors[2]),
      ),
      listOf(
        BubbleChartData.BubbleData(8f, 20f, 22.5f, colors[3]),
        BubbleChartData.BubbleData(12f, 30f, 30f, colors[3]),
        BubbleChartData.BubbleData(30f, 40f, 45f, colors[3]),
      ),
    ),
  )

private fun getBubbleChartRenderer(colors: List<Color>) =
  SimpleBubbleChartDataRenderer(getBubbleChartData(colors = colors))

@Composable
fun BubbleChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  BubbleChart(
    renderer = getBubbleChartRenderer(colors = colors),
    modifier = modifier.size(300.dp),
  )
}
