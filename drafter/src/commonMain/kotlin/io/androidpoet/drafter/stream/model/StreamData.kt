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
package io.androidpoet.drafter.stream.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * A single flowing band in a stream graph (themeriver).
 *
 * @param name human-readable series name
 * @param values one value per x point; must match the chart's [StreamData.labels] size
 * @param color the band fill colour
 */
@Immutable
public data class StreamSeries(
  val name: String,
  val values: List<Float>,
  val color: Color,
)

/**
 * Data for a stream graph (themeriver): several [series] stacked and centred
 * around a wiggle baseline so they flow like a river.
 *
 * @param labels x-axis labels, one per x point
 * @param series the stacked flowing bands; all share [labels].size values
 */
@Immutable
public data class StreamData(
  val labels: List<String>,
  val series: List<StreamSeries>,
)
