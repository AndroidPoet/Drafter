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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.bars.BarChart
import io.androidpoet.drafter.bars.model.StackedBarChartData
import io.androidpoet.drafter.bars.renderer.StackedBarChartRenderer
import io.androidpoet.drafterdemo.ChartTitle

private fun getStackedBarChartData() =
  StackedBarChartData(
    labelsList = listOf("Q1", "Q2", "Q3"),
    stacks =
      listOf(
        listOf(10f, 15f, 5f), // Q1
        listOf(8f, 12f, 20f), // Q2
        listOf(18f, 10f, 15f), // Q3
      ),
    colors = listOf(Color.Red, Color.Green, Color.Blue),
  )

private fun getStackedBarChartRenderer() = StackedBarChartRenderer(getStackedBarChartData())

@Composable
fun StackedBarChartExample(modifier: Modifier = Modifier) {
  ChartTitle(text = "Stacked Bar Chart")

  BarChart(
    renderer = getStackedBarChartRenderer(),
    modifier =
      modifier
        .height(300.dp)
        .fillMaxWidth(),
    animate = true,
  )
}
