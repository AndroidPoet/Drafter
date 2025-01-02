package io.androidpoet.drafterdemo.githubgraph

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.heatmap.ContributionData
import io.androidpoet.drafter.heatmap.ContributionHeatmap
import io.androidpoet.drafter.heatmap.ContributionHeatmapData
import io.androidpoet.drafterdemo.ChartTitle
import kotlinx.datetime.Clock
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

@Composable
fun GithubGraph() {
  val now = Clock.System.now()
  val contributions = remember {
    buildList {
      // Generate data for the entire year
      repeat(365) { day ->
        val date = now.minus(day.days)
        // Random contribution count (0-15)
        val count = if (Random.nextFloat() > 0.6f) Random.nextInt(1, 15) else 0
        add(ContributionData(date, count))
      }
    }
  }

  val data = ContributionHeatmapData(contributions)


  ChartTitle(text = "Github Graph")
  ContributionHeatmap(
    data = data,
    modifier = Modifier
      .fillMaxWidth()
      .height(112.dp)
  )
}