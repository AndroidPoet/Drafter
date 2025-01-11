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
package io.androidpoet.drafter.radar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.radar.renderer.RadarChartRenderer

@Composable
public fun RadarChart(
  renderer: RadarChartRenderer,
  modifier: Modifier = Modifier,
  isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
) {
  val textMeasurer = rememberTextMeasurer()
  val animatedProgress = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    animatedProgress.animateTo(
      targetValue = 1f,
      animationSpec =
      tween(
        durationMillis = 1000,
        easing = FastOutSlowInEasing,
      ),
    )
  }

  Canvas(modifier = modifier.size(300.dp)) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.width.coerceAtMost(size.height) / 2 * 0.8f

    renderer.drawChart(
      drawScope = this,
      centerX = centerX,
      centerY = centerY,
      radius = radius,
      textMeasurer = textMeasurer,
      animationProgress = animatedProgress.value,
      isSystemInDarkTheme = isSystemInDarkTheme,
    )
  }
}
