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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.baselineprofile.app.ChartTitle
import io.androidpoet.drafter.pie.PieChart
import io.androidpoet.drafter.pie.PieChartData

@Composable
fun PieChartExample() {
  ChartTitle(text = "Pie Chart")
  val data = PieChartData(
    slices = listOf(
      PieChartData.Slice(40f, Color.Blue, "Blue"),
      PieChartData.Slice(30f, Color.Red, "Red"),
      PieChartData.Slice(20f, Color.Green, "Green"),
      PieChartData.Slice(10f, Color.Yellow, "Yellow"),
    ),
  )
  Box(
    modifier = Modifier.size(200.dp).padding(horizontal = 16.dp),
    contentAlignment = Alignment.Center,
  ) {
    PieChart(
      data = data,
      modifier = Modifier.fillMaxSize(),
    )
  }
}
