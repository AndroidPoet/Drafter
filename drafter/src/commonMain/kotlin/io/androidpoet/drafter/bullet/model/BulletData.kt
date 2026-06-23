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
package io.androidpoet.drafter.bullet.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.theme.DrafterColors

@Immutable
public data class BulletMetric(
  val label: String,
  val value: Float,
  val target: Float,
  val ranges: List<Float>,
  val color: Color = DrafterColors.Indigo,
)

@Immutable
public data class BulletData(
  val metrics: List<BulletMetric>,
)
