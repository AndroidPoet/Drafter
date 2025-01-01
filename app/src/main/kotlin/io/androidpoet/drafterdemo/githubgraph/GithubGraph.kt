package io.androidpoet.drafterdemo.githubgraph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.heatmap.ContributionData
import io.androidpoet.drafter.heatmap.ContributionHeatmap
import io.androidpoet.drafter.heatmap.ContributionHeatmapData
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

val now = Clock.System.now()
// Create sample data with varying contribution levels
val contributions = listOf(
  ContributionData(now, 12),                    // Level 4 (bright green)
  ContributionData(now.minus(1.days), 8),       // Level 3
  ContributionData(now.minus(2.days), 5),       // Level 2
  ContributionData(now.minus(3.days), 2),       // Level 1
  ContributionData(now.minus(4.days), 0)        // Empty
)

val data = ContributionHeatmapData(contributions)

@Composable
fun GithubGraph() {
  ContributionHeatmap(
    data = data,
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
  )
}