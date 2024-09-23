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
package io.androidpoet.drafter.pie

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer

@Composable
public fun PieChart(
  data: PieChartData,
  modifier: Modifier = Modifier,
  animate: Boolean = true,
) {
  val textMeasurer = rememberTextMeasurer()
  val progress = remember { Animatable(0f) }

  LaunchedEffect(animate) {
    if (animate) {
      progress.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
      )
    } else {
      progress.snapTo(1f)
    }
  }

  Canvas(modifier = modifier) {
    val size = this.size
    val renderer = PieChartRenderer(data, size, textMeasurer, progress.value)

    renderer.drawPieChart(this)
  }
}

@Composable
public fun DonutChart(
  data: PieChartData,
  modifier: Modifier = Modifier,
  animate: Boolean = true,
) {
  val textMeasurer = rememberTextMeasurer()
  val progress = remember { Animatable(0f) }

  LaunchedEffect(animate) {
    if (animate) {
      progress.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
      )
    } else {
      progress.snapTo(1f)
    }
  }

  Canvas(modifier = modifier) {
    val size = this.size
    val renderer = DonutChartRenderer(data, size, textMeasurer, progress.value)

    renderer.drawDonutChart(this)
  }
}
