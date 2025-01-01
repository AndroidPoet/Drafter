package io.androidpoet.drafter.heatmap

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Instant

public data class ContributionData(
  val timestamp: Instant,
  val count: Int
)

public data class ContributionHeatmapData(
  val contributions: List<ContributionData>,
  val baseColor: Color = Color(0xFF40C463), // GitHub's green color
  val backgroundSquareColor: Color = Color(0xFF2D333B) // Dark background for squares
)