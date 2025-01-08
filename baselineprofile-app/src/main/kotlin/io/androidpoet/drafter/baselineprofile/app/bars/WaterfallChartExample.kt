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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.bars.BarChart
import io.androidpoet.drafter.bars.model.WaterfallChartData
import io.androidpoet.drafter.bars.renderer.WaterfallChartRenderer
import io.androidpoet.drafter.baselineprofile.app.ChartTitle

private fun getWaterfallChartRenderer() = WaterfallChartRenderer(
  WaterfallChartData(
    labelsList = listOf("Start", "Revenue", "Cost", "Profit"),
    values = listOf(+50f, -20f, +30f), // Changes from 'Start'
    colors = listOf(Color.Green, Color.Red, Color.Green),
    initialValue = 100f, // Start from 100
  ),
)

@Composable
fun WaterfallChartExample(modifier: Modifier = Modifier) {
  ChartTitle(text = "Waterfall Chart")

  BarChart(
    renderer = getWaterfallChartRenderer(),
    modifier = Modifier
      .height(300.dp)
      .fillMaxWidth(),
    animate = true,
  )
}
