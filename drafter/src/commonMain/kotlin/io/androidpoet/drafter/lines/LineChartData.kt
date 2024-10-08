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
package io.androidpoet.drafter.lines

import androidx.compose.ui.graphics.Color

public interface LineChartData {
  public val labels: List<String>
}

public data class SimpleLineChartData(
  override val labels: List<String>,
  val values: List<Float>,
  val color: Color,
) : LineChartData

public data class GroupedLineChartData(
  override val labels: List<String>,
  val itemNames: List<String>,
  val groupedValues: List<List<Float>>,
  val colors: List<Color>,
) : LineChartData

public data class StackedLineChartData(
  override val labels: List<String>,
  val stacks: List<List<Float>>,
  val colors: List<Color>,
) : LineChartData
