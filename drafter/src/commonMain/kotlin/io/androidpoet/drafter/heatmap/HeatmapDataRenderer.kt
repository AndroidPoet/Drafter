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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

public interface HeatmapDataRenderer {
  public val data: ContributionHeatmapData

  public fun drawHeatmap(
    drawScope: DrawScope,
    cellSize: Float,
    cellPadding: Float,
    startInstant: Instant,
    endInstant: Instant,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
  )
}

public class HeatmapRenderer(
  override val data: ContributionHeatmapData,
) : HeatmapDataRenderer {
  private fun getContributionColor(
    count: Int,
    isSystemInDarkTheme: Boolean,
  ): Color =
    when {
      count == 0 ->
        if (isSystemInDarkTheme) {
          Color(0xFF24292E)
        } else {
          Color(0xFFF6F8FA).copy(alpha = 0.5f)
        }
      count <= 3 -> data.baseColor.copy(alpha = 0.2f)
      count <= 6 -> data.baseColor.copy(alpha = 0.4f)
      count <= 9 -> data.baseColor.copy(alpha = 0.7f)
      else -> data.baseColor.copy(alpha = 1.0f)
    }

  override fun drawHeatmap(
    drawScope: DrawScope,
    cellSize: Float,
    cellPadding: Float,
    startInstant: Instant,
    endInstant: Instant,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
  ) {
    with(drawScope) {
      val startDate = startInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
      val endDate = endInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
      val weeks = startDate.daysUntil(endDate) / 7

      val contributionsMap =
        data.contributions
          .groupingBy {
            it.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).date
          }.aggregate { _, accumulator: Int?, element, _ ->
            (accumulator ?: 0) + element.count
          }

      var currentDate = startDate
      for (week in 0..weeks) {
        for (dayOfWeek in 0..6) {
          if (currentDate <= endDate) {
            val contributions = contributionsMap[currentDate] ?: 0

            val x = week * (cellSize + cellPadding)
            val y = dayOfWeek * (cellSize + cellPadding)

            drawRect(
              color = getContributionColor(contributions, isSystemInDarkTheme),
              topLeft = Offset(x, y),
              size = Size(cellSize, cellSize),
            )

            currentDate = currentDate.plus(1, DateTimeUnit.DAY)
          }
        }
      }
    }
  }
}
