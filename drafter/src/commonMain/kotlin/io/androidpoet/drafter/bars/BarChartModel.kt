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
package io.androidpoet.drafter.bars

import androidx.compose.ui.graphics.Color

public data class WaterfallChartData(
  override val labels: List<String>,
  val values: List<Float>, // The values (positive or negative) for each step
  val colors: List<Color>, // Colors for each bar
  val initialValue: Float = 0f, // Optional starting value
) : BarChartData

// Simple bar chart data
public data class SimpleBarChartData(
  override val labels: List<String>,
  val values: List<Float>,
  val colors: List<Color>,
) : BarChartData

// Grouped bar chart data
public data class GroupedBarChartData(
  override val labels: List<String>,
  val itemNames: List<String>,
  val groupedValues: List<List<Float>>,
  val colors: List<Color>,
) : BarChartData

// Stacked bar chart data
public data class StackedBarChartData(
  override val labels: List<String>,
  val stacks: List<List<Float>>,
  val colors: List<Color>,
) : BarChartData