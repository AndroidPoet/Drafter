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
package io.androidpoet.drafter.histogram

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.bars.BarChart

@Composable
public fun HistogramChart(
  dataPoints: List<Float>,
  binCount: Int,
  color: Color = Color.Blue,
  modifier: Modifier = Modifier,
) {
  val histogramData = createHistogramData(dataPoints, binCount, color)
  val histogramRenderer = HistogramRenderer()

  BarChart(
    data = histogramData,
    renderer = histogramRenderer,
    modifier = modifier,
  )
}
