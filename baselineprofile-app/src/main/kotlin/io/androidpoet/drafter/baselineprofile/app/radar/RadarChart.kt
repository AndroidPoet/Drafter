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
package io.androidpoet.drafter.baselineprofile.app.radar

import RadarChartData
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.radar.RadarChart
import io.androidpoet.drafter.baselineprofile.app.ChartContainer
import io.androidpoet.drafter.baselineprofile.app.ChartTitle

@Composable
fun RadarChartExample() {
  ChartTitle(text = "Radar Chart")
  val data = listOf(
    RadarChartData(
      mapOf(
        "Execution" to 0.8f,
        "Landing" to 0.6f,
        "Difficulty" to 0.9f,
        "Style" to 0.7f,
        "Creativity" to 0.85f,
      ),
    ),
  )
  val colors = listOf(Color.Blue, Color.Red)

  RadarChart(
    data = data,
    colors = colors,
  )
}
