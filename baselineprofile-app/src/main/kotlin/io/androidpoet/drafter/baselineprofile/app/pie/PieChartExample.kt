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
package io.androidpoet.drafter.baselineprofile.app.pie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.baselineprofile.app.ChartTitle
import io.androidpoet.drafter.pie.PieChart
import io.androidpoet.drafter.pie.model.PieChartData
import io.androidpoet.drafter.pie.renderer.DonutChartRenderer
import io.androidpoet.drafter.pie.renderer.PieChartRenderer

private fun getPieChartRenderer() =
  PieChartRenderer(
    PieChartData(
      slices =
        listOf(
          PieChartData.Slice(value = 40f, color = Color.Red, label = "Red"),
          PieChartData.Slice(value = 30f, color = Color.Green, label = "Green"),
          PieChartData.Slice(value = 20f, color = Color.Blue, label = "Blue"),
          PieChartData.Slice(value = 10f, color = Color.Magenta, label = "Purple"),
        ),
    ),
  )

private fun getDonutPieChartRenderer() =
  DonutChartRenderer(
    PieChartData(
      slices =
        listOf(
          PieChartData.Slice(value = 40f, color = Color.Red, label = "Red"),
          PieChartData.Slice(value = 30f, color = Color.Green, label = "Green"),
          PieChartData.Slice(value = 20f, color = Color.Blue, label = "Blue"),
          PieChartData.Slice(value = 10f, color = Color.Magenta, label = "Purple"),
        ),
    ),
  )

@Composable
fun PieChartExample() {
  ChartTitle(text = "Pie Chart")
  PieChart(
    renderer = getPieChartRenderer(),
    modifier = Modifier,
    animate = true,
  )
}

@Composable
fun DonutChartExample() {
  ChartTitle(text = "Pie Chart")
  PieChart(
    renderer = getDonutPieChartRenderer(),
    modifier = Modifier,
    animate = true,
  )
}
