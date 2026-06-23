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
package io.androidpoet.drafter.bars.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Data class representing data for a grouped bar chart where multiple bars are shown side by side
 * for each category/label.
 *
 * @property labelsList List of labels for each group on the X-axis
 * @property itemNames Names of individual items within each group (e.g., "Men", "Women" or "Q1", "Q2", "Q3")
 * @property groupedValues List of lists containing values for each item in each group
 * @property colors List of colors to be used for different items in the groups
 */
@Immutable
public data class GroupedBarChartData(
  val labelsList: List<String>,
  val itemNames: List<String>,
  val groupedValues: List<List<Float>>,
  val colors: List<Color>,
)

/**
 * Data class representing data for a simple bar chart with single bars.
 *
 * @property labelsList List of labels for the X-axis
 * @property values List of values for each bar
 * @property colors List of colors for each bar
 */
@Immutable
public data class SimpleBarChartData(
  val labelsList: List<String>,
  val values: List<Float>,
  val colors: List<Color>,
)

/**
 * Data class representing data for a stacked bar chart where bars are stacked on top of each other.
 *
 * @property labelsList List of labels for the X-axis
 * @property stacks List of lists containing values for each stack segment
 * @property colors List of colors for each stack segment
 */
@Immutable
public data class StackedBarChartData(
  val labelsList: List<String>,
  val stacks: List<List<Float>>,
  val colors: List<Color>,
)

/**
 * Data class representing data for a waterfall chart that shows running total.
 *
 * @property labelsList List of labels for the X-axis
 * @property values List of values representing changes at each step
 * @property colors List of colors for each bar
 * @property initialValue Starting value for the waterfall chart (defaults to 0)
 */
@Immutable
public data class WaterfallChartData(
  val labelsList: List<String>,
  val values: List<Float>,
  val colors: List<Color>,
  val initialValue: Float = 0f,
)

/**
 * Data class representing histogram data with binned values.
 *
 * @property labels List of bin labels for the X-axis
 * @property frequencies List of frequencies/counts for each bin
 * @property colors List of colors for each bin
 */
@Immutable
public data class HistogramData(
  val labels: List<String>,
  val frequencies: List<Float>,
  val colors: List<Color>,
)
