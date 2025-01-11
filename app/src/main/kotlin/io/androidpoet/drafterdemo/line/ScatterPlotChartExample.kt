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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.scatterplot.ScatterPlot
import io.androidpoet.drafter.scatterplot.SimpleScatterPlotRenderer
import io.androidpoet.drafter.scatterplot.model.ScatterPlotData
import kotlin.random.Random

private fun getScatterPlotRenderer(colors: List<Color>) =
  SimpleScatterPlotRenderer(
    ScatterPlotData(
      points =
      List(30) {
        Pair(
          Random.nextFloat() * 50f,
          Random.nextFloat() * 50f,
        )
      },
      pointColors =
      List(30) {
        if (colors.isNotEmpty()) colors[it % colors.size] else Color.Gray
      },
    ),
  )

@Composable
fun ScatterPlotChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  ScatterPlot(
    modifier =
    Modifier
      .height(300.dp)
      .fillMaxWidth(),
    renderer = getScatterPlotRenderer(colors = colors),
  )
}
