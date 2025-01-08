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
import io.androidpoet.drafter.bars.model.SimpleBarChartData
import io.androidpoet.drafter.bars.renderer.BarChartRenderer
import io.androidpoet.drafter.baselineprofile.app.ChartTitle

private fun getBarChartData() = SimpleBarChartData(
  labelsList = listOf("Jan", "Feb", "Mar", "Apr"),
  values = listOf(10f, 30f, 15f, 45f),
  colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta),
)

private fun getSimpleBarChartRenderer() = BarChartRenderer(getBarChartData())

@Composable
fun SimpleBarChartExample() {
  ChartTitle(text = "Simple Bar Chart")

  BarChart(
    renderer = getSimpleBarChartRenderer(),
    modifier = Modifier
      .height(300.dp)
      .fillMaxWidth(),
    animate = true,
  )
}
