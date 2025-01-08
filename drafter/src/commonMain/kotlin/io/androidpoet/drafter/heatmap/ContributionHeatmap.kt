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
package io.androidpoet.drafter.heatmap

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

@Composable
public fun ContributionHeatmap(
  renderer: HeatmapRenderer,
  modifier: Modifier = Modifier,
) {
  val density = LocalDensity.current
  val cellSize = with(density) { 8.dp.toPx() }
  val cellPadding = with(density) { 2.dp.toPx() }

  val now = Clock.System.now()
  val endDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
  val startDate = endDate.minus(1, DateTimeUnit.YEAR)

  val startInstant = startDate.atStartOfDayIn(TimeZone.currentSystemDefault())
  val endInstant = endDate.atStartOfDayIn(TimeZone.currentSystemDefault())

  val animationProgress = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    animationProgress.animateTo(
      targetValue = 1f,
      animationSpec =
        tween(
          durationMillis = 1000,
          easing = FastOutSlowInEasing,
        ),
    )
  }

  Box(
    modifier =
      modifier
        .horizontalScroll(rememberScrollState())
        .background(Color(0xFF0D1117))
        .padding(8.dp),
  ) {
    Canvas(
      modifier =
        Modifier
          .fillMaxSize()
          .padding(4.dp),
    ) {
      renderer.drawHeatmap(
        drawScope = this,
        cellSize = cellSize,
        cellPadding = cellPadding,
        startInstant = startInstant,
        endInstant = endInstant,
        animationProgress = animationProgress.value,
      )
    }
  }
}
