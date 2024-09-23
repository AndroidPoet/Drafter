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
package io.androidpoet.drafter.baselineprofile.app.line

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.baselineprofile.app.ChartContainer
import io.androidpoet.drafter.baselineprofile.app.ChartTitle
import io.androidpoet.drafter.scatterplot.ScatterPlot
import io.androidpoet.drafter.scatterplot.ScatterPlotData
import io.androidpoet.drafter.scatterplot.SimpleScatterPlotRenderer
import kotlin.random.Random

@Composable
fun ScatterPlotChartExample() {
  ChartTitle(text = "Scatter Plot Chart")
  val numberOfPoints = 30
  val randomPoints = List(numberOfPoints) {
    Pair(
      Random.nextFloat() * 50f,
      Random.nextFloat() * 50f,
    )
  }
  val randomColors = List(numberOfPoints) {
    Color(
      red = Random.nextFloat(),
      green = Random.nextFloat(),
      blue = Random.nextFloat(),
      alpha = 1f,
    )
  }
  val data = ScatterPlotData(
    points = randomPoints,
    pointColors = randomColors,
  )
  val renderer = SimpleScatterPlotRenderer()
  ChartContainer(modifier = Modifier.height(300.dp)) {
    ScatterPlot(
      data = data,
      renderer = renderer,
      modifier = Modifier.fillMaxSize(),
    )
  }
}
