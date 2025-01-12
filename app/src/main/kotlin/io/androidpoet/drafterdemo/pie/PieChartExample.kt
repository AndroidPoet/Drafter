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
package io.androidpoet.drafterdemo.pie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.pie.PieChart
import io.androidpoet.drafter.pie.model.PieChartData
import io.androidpoet.drafter.pie.renderer.DonutChartRenderer
import io.androidpoet.drafter.pie.renderer.PieChartRenderer

private fun getPieChartRenderer(colors: List<Color>) =
  PieChartRenderer(
    PieChartData(
      slices =
        listOf(
          PieChartData.Slice(value = 40f, color = colors[0], label = "Red"),
          PieChartData.Slice(value = 30f, color = colors[1], label = "Green"),
          PieChartData.Slice(value = 20f, color = colors[2], label = "Blue"),
          PieChartData.Slice(value = 10f, color = colors[3], label = "Purple"),
        ),
    ),
  )

private fun getDonutPieChartRenderer(colors: List<Color>) =
  DonutChartRenderer(
    PieChartData(
      slices =
      listOf(
        PieChartData.Slice(value = 40f, color = colors[0], label = "Red"),
        PieChartData.Slice(value = 30f, color = colors[1], label = "Green"),
        PieChartData.Slice(value = 20f, color = colors[2], label = "Blue"),
        PieChartData.Slice(value = 10f, color = colors[3], label = "Purple"),
      ),
    ),
  )

@Composable
fun PieChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  PieChart(
    renderer = getPieChartRenderer(colors = colors),
    modifier =
    modifier
      .height(300.dp).fillMaxWidth(),
    animate = true,
  )
}

@Composable
fun DonutChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  PieChart(
    renderer = getDonutPieChartRenderer(colors = colors),
    modifier =
    modifier
      .height(300.dp)
      .fillMaxWidth(),
    animate = true,
  )
}
