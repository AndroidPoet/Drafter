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
import kotlinx.datetime.*

public interface HeatmapRenderer {
  public fun drawHeatmap(
    drawScope: DrawScope,
    data: ContributionHeatmapData,
    cellSize: Float,
    cellPadding: Float,
    startInstant: Instant,
    endInstant: Instant,
    animationProgress: Float
  )
}

public class DefaultHeatmapRenderer : HeatmapRenderer {

  // GitHub's actual contribution level colors
  private val emptyColor = Color(0xFF2D333B)    // Dark background for empty squares
  private val level1Color = Color(0xFF0E4429)   // Least contributions
  private val level2Color = Color(0xFF006D32)
  private val level3Color = Color(0xFF26A641)
  private val level4Color = Color(0xFF39D353)   // Most contributions

  private fun getContributionColor(count: Int): Color {
    return when {
      count == 0 -> emptyColor
      count <= 3 -> level1Color
      count <= 6 -> level2Color
      count <= 9 -> level3Color
      else -> level4Color
    }
  }

  override fun drawHeatmap(
    drawScope: DrawScope,
    data: ContributionHeatmapData,
    cellSize: Float,
    cellPadding: Float,
    startInstant: Instant,
    endInstant: Instant,
    animationProgress: Float
  ) {
    with(drawScope) {
      val startDate = startInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
      val endDate = endInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
      val weeks = startDate.daysUntil(endDate) / 7

      val contributionsMap: Map<Long, Int> = data.contributions.associate {
        it.timestamp.toEpochMilliseconds() to it.count
      }

      var currentDate = startDate
      for (week in 0..weeks) {
        val weekProgress = (week.toFloat() / weeks).coerceIn(0f, 1f)
        if (weekProgress <= animationProgress) {
          for (dayOfWeek in 0..6) {
            if (currentDate <= endDate) {
              val currentInstant = currentDate
                .atStartOfDayIn(TimeZone.currentSystemDefault())

              val contributions = contributionsMap[currentInstant.toEpochMilliseconds()] ?: 0

              val x = week * (cellSize + cellPadding)
              val y = dayOfWeek * (cellSize + cellPadding)

              // Draw cell with appropriate color based on contributions
              drawRect(
                color = getContributionColor(contributions),
                topLeft = Offset(x, y),
                size = Size(cellSize, cellSize),
                alpha = 1f // Full opacity always
              )

              currentDate = currentDate.plus(1, DateTimeUnit.DAY)
            }
          }
        }
      }
    }
  }
}