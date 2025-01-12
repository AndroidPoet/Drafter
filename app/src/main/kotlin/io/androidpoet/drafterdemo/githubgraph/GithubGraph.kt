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
package io.androidpoet.drafterdemo.githubgraph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.heatmap.ContributionData
import io.androidpoet.drafter.heatmap.ContributionHeatmapData
import io.androidpoet.drafter.heatmap.Heatmap
import io.androidpoet.drafter.heatmap.HeatmapRenderer
import kotlinx.datetime.Clock
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

private fun getHeatmapRenderer(color: Color) =
  HeatmapRenderer(
    ContributionHeatmapData(
      baseColor = color,
      contributions =
        buildList {
          val now = Clock.System.now()
          repeat(365) { day ->
            val date = now.minus(day.days)
            val count = if (Random.nextFloat() > 0.6f) Random.nextInt(1, 15) else 0
            add(ContributionData(date, count))
          }
        },
    ),
  )

@Composable
fun GithubGraph(
  color: Color,
  modifier: Modifier = Modifier,
) {
  Heatmap(
    renderer = getHeatmapRenderer(color = color),
    modifier =
      modifier
        .height(300.dp)
        .fillMaxWidth(),
  )
}
