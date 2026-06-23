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
package io.androidpoet.drafterdemo.shared

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.area.AreaChart
import io.androidpoet.drafter.area.AreaChartRenderer
import io.androidpoet.drafter.area.model.AreaChartData
import io.androidpoet.drafter.bars.BarChart
import io.androidpoet.drafter.bars.model.GroupedBarChartData
import io.androidpoet.drafter.bars.model.SimpleBarChartData
import io.androidpoet.drafter.bars.model.StackedBarChartData
import io.androidpoet.drafter.bars.model.WaterfallChartData
import io.androidpoet.drafter.bars.renderer.BarChartRenderer
import io.androidpoet.drafter.bars.renderer.GroupedBarChartRenderer
import io.androidpoet.drafter.bars.renderer.HistogramRenderer
import io.androidpoet.drafter.bars.renderer.StackedBarChartRenderer
import io.androidpoet.drafter.bars.renderer.WaterfallChartRenderer
import io.androidpoet.drafter.boxplot.BoxPlotChart
import io.androidpoet.drafter.boxplot.BoxPlotChartRenderer
import io.androidpoet.drafter.boxplot.model.BoxGroup
import io.androidpoet.drafter.boxplot.model.BoxPlotData
import io.androidpoet.drafter.bubble.BubbleChart
import io.androidpoet.drafter.bubble.BubbleChartData
import io.androidpoet.drafter.bubble.SimpleBubbleChartDataRenderer
import io.androidpoet.drafter.bullet.BulletChart
import io.androidpoet.drafter.bullet.BulletChartRenderer
import io.androidpoet.drafter.bullet.model.BulletData
import io.androidpoet.drafter.bullet.model.BulletMetric
import io.androidpoet.drafter.candlestick.CandlestickChart
import io.androidpoet.drafter.candlestick.CandlestickChartRenderer
import io.androidpoet.drafter.candlestick.model.Candle
import io.androidpoet.drafter.candlestick.model.CandlestickData
import io.androidpoet.drafter.funnel.FunnelChart
import io.androidpoet.drafter.funnel.FunnelChartRenderer
import io.androidpoet.drafter.funnel.model.FunnelData
import io.androidpoet.drafter.funnel.model.FunnelStage
import io.androidpoet.drafter.gantt.GanttChart
import io.androidpoet.drafter.gantt.GanttChartData
import io.androidpoet.drafter.gantt.GanttChartRenderer
import io.androidpoet.drafter.gantt.GanttTask
import io.androidpoet.drafter.gauge.GaugeChart
import io.androidpoet.drafter.gauge.GaugeChartRenderer
import io.androidpoet.drafter.gauge.model.GaugeData
import io.androidpoet.drafter.heatmap.ContributionData
import io.androidpoet.drafter.heatmap.ContributionHeatmapData
import io.androidpoet.drafter.heatmap.Heatmap
import io.androidpoet.drafter.heatmap.HeatmapRenderer
import io.androidpoet.drafter.lines.LineChart
import io.androidpoet.drafter.lines.model.GroupedLineChartData
import io.androidpoet.drafter.lines.model.SimpleLineChartData
import io.androidpoet.drafter.lines.model.StackedLineChartData
import io.androidpoet.drafter.lines.renderer.GroupedLineChartRenderer
import io.androidpoet.drafter.lines.renderer.LineChartRenderer
import io.androidpoet.drafter.lines.renderer.StackedLineChartRenderer
import io.androidpoet.drafter.pie.PieChart
import io.androidpoet.drafter.pie.model.PieChartData
import io.androidpoet.drafter.pie.renderer.DonutChartRenderer
import io.androidpoet.drafter.pie.renderer.PieChartRenderer
import io.androidpoet.drafter.polar.PolarAreaChart
import io.androidpoet.drafter.polar.PolarAreaChartRenderer
import io.androidpoet.drafter.polar.model.PolarAreaData
import io.androidpoet.drafter.polar.model.PolarSlice
import io.androidpoet.drafter.radar.RadarChart
import io.androidpoet.drafter.radar.model.RadarChartData
import io.androidpoet.drafter.radar.renderer.RadarChartRenderer
import io.androidpoet.drafter.sankey.SankeyChart
import io.androidpoet.drafter.sankey.SankeyChartRenderer
import io.androidpoet.drafter.sankey.model.SankeyData
import io.androidpoet.drafter.sankey.model.SankeyLink
import io.androidpoet.drafter.sankey.model.SankeyNode
import io.androidpoet.drafter.scatterplot.ScatterPlot
import io.androidpoet.drafter.scatterplot.SimpleScatterPlotRenderer
import io.androidpoet.drafter.scatterplot.model.ScatterPlotData
import io.androidpoet.drafter.stepline.StepLineChart
import io.androidpoet.drafter.stepline.StepLineChartRenderer
import io.androidpoet.drafter.stepline.model.StepLineChartData
import io.androidpoet.drafter.stream.StreamGraphChart
import io.androidpoet.drafter.stream.StreamGraphChartRenderer
import io.androidpoet.drafter.stream.model.StreamData
import io.androidpoet.drafter.stream.model.StreamSeries
import io.androidpoet.drafter.sunburst.SunburstChart
import io.androidpoet.drafter.sunburst.SunburstChartRenderer
import io.androidpoet.drafter.sunburst.model.SunburstData
import io.androidpoet.drafter.sunburst.model.SunburstNode
import io.androidpoet.drafter.theme.DrafterColors
import io.androidpoet.drafter.treemap.TreemapChart
import io.androidpoet.drafter.treemap.TreemapChartRenderer
import io.androidpoet.drafter.treemap.model.TreemapData
import io.androidpoet.drafter.treemap.model.TreemapItem
import kotlinx.datetime.Clock
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

private val palette: List<Color> = DrafterColors.palette
private const val DARK = false

private val SurfaceColor = Color(0xFFF4F6FA)
private val CardColor = Color.White
private val TitleColor = Color(0xFF1B1E25)
private val SubtitleColor = Color(0xFF6B7280)

private val pieSlices: List<PieChartData.Slice> = listOf(
  PieChartData.Slice(value = 40f, color = palette[0], label = "Mobile"),
  PieChartData.Slice(value = 30f, color = palette[1], label = "Desktop"),
  PieChartData.Slice(value = 20f, color = palette[2], label = "Web"),
  PieChartData.Slice(value = 10f, color = palette[3], label = "Other"),
)

private data class ChartEntry(val title: String, val content: @Composable () -> Unit)

private fun chartEntries(): List<ChartEntry> = listOf(
  ChartEntry("Bar") {
    BarChart(
      renderer = BarChartRenderer(
        SimpleBarChartData(
          labelsList = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
          values = listOf(10f, 30f, 15f, 45f, 28f, 52f),
          colors = palette,
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Grouped Bar") {
    BarChart(
      renderer = GroupedBarChartRenderer(
        GroupedBarChartData(
          labelsList = listOf("2020", "2021", "2022"),
          itemNames = listOf("Product A", "Product B", "Product C"),
          groupedValues = listOf(
            listOf(10f, 20f, 15f),
            listOf(25f, 5f, 30f),
            listOf(12f, 28f, 10f),
          ),
          colors = palette,
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Stacked Bar") {
    BarChart(
      renderer = StackedBarChartRenderer(
        StackedBarChartData(
          labelsList = listOf("Q1", "Q2", "Q3"),
          stacks = listOf(
            listOf(10f, 15f, 5f),
            listOf(8f, 12f, 20f),
            listOf(18f, 10f, 15f),
          ),
          colors = palette,
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Histogram") {
    BarChart(
      renderer = HistogramRenderer(
        dataPoints = listOf(1f, 2f, 2f, 3f, 3f, 3f, 4f, 4f, 5f, 5f, 5f, 5f),
        binCount = 5,
        color = palette[0],
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Waterfall") {
    BarChart(
      renderer = WaterfallChartRenderer(
        WaterfallChartData(
          labelsList = listOf("Start", "Revenue", "Cost", "Profit"),
          values = listOf(+50f, -20f, +30f),
          colors = palette,
          initialValue = 100f,
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Line") {
    LineChart(
      renderer = LineChartRenderer(
        SimpleLineChartData(
          labels = listOf("A", "B", "C", "D", "E", "F"),
          values = listOf(10f, 20f, 15f, 25f, 18f, 30f),
          color = palette.first(),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
    )
  },
  ChartEntry("Grouped Line") {
    LineChart(
      renderer = GroupedLineChartRenderer(
        GroupedLineChartData(
          labels = listOf("Q1", "Q2", "Q3", "Q4"),
          itemNames = listOf("Product A", "Product B"),
          groupedValues = listOf(
            listOf(10f, 15f),
            listOf(20f, 25f),
            listOf(15f, 10f),
            listOf(25f, 20f),
          ),
          colors = palette,
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
    )
  },
  ChartEntry("Stacked Line") {
    LineChart(
      renderer = StackedLineChartRenderer(
        StackedLineChartData(
          labels = listOf("Jan", "Feb", "Mar", "Apr"),
          stacks = listOf(
            listOf(5f, 5f, 2f),
            listOf(7f, 3f, 4f),
            listOf(6f, 4f, 3f),
            listOf(8f, 2f, 5f),
          ),
          colors = palette,
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
    )
  },
  ChartEntry("Scatter Plot") {
    ScatterPlot(
      renderer = SimpleScatterPlotRenderer(
        ScatterPlotData(
          points = List(30) {
            Pair(
              (Random.nextFloat() * 10).roundToInt() / 10f,
              (Random.nextFloat() * 10).roundToInt() / 10f,
            )
          },
          pointColors = List(30) { palette[it % palette.size] },
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
    )
  },
  ChartEntry("Bubble") {
    BubbleChart(
      renderer = SimpleBubbleChartDataRenderer(
        BubbleChartData(
          series = listOf(
            listOf(
              BubbleChartData.BubbleData(10f, 26f, 30f, palette[0]),
              BubbleChartData.BubbleData(26f, 30f, 60f, palette[0]),
              BubbleChartData.BubbleData(26f, 46f, 45f, palette[0]),
            ),
            listOf(
              BubbleChartData.BubbleData(14f, 15f, 30f, palette[1]),
              BubbleChartData.BubbleData(22f, 36f, 45f, palette[1]),
              BubbleChartData.BubbleData(90f, 57f, 75f, palette[1]),
            ),
            listOf(
              BubbleChartData.BubbleData(8f, 9f, 90f, palette[2]),
              BubbleChartData.BubbleData(20f, 57f, 45f, palette[2]),
              BubbleChartData.BubbleData(40f, 50f, 60f, palette[2]),
            ),
            listOf(
              BubbleChartData.BubbleData(8f, 20f, 22.5f, palette[3]),
              BubbleChartData.BubbleData(12f, 30f, 30f, palette[3]),
              BubbleChartData.BubbleData(30f, 40f, 45f, palette[3]),
            ),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Pie") {
    PieChart(
      renderer = PieChartRenderer(PieChartData(slices = pieSlices)),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Donut") {
    PieChart(
      renderer = DonutChartRenderer(PieChartData(slices = pieSlices)),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Radar") {
    RadarChart(
      renderer = RadarChartRenderer(
        data = listOf(
          RadarChartData(
            mapOf(
              "Execution" to 0.8f,
              "Landing" to 0.6f,
              "Difficulty" to 0.9f,
              "Style" to 0.7f,
              "Creativity" to 0.85f,
            ),
          ),
        ),
        colors = palette,
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
    )
  },
  ChartEntry("Gantt") {
    GanttChart(
      renderer = GanttChartRenderer(
        GanttChartData(
          taskColors = palette,
          tasks = listOf(
            GanttTask("Planning", 0f, 2f),
            GanttTask("Design", 2f, 2f),
            GanttTask("Development", 4f, 3f),
            GanttTask("Testing", 7f, 2f),
            GanttTask("Deployment", 9f, 1f),
          ),
        ),
      ),
      modifier = chartModifier(),
      animate = true,
      isSystemInDarkTheme = DARK,
    )
  },
  ChartEntry("Contribution Heatmap") {
    Heatmap(
      renderer = HeatmapRenderer(
        ContributionHeatmapData(
          baseColor = palette[4],
          contributions = buildList {
            val now = Clock.System.now()
            repeat(365) { day ->
              val date = now.minus(day.days)
              val count = if (Random.nextFloat() > 0.6f) Random.nextInt(1, 15) else 0
              add(ContributionData(date, count))
            }
          },
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
    )
  },
  ChartEntry("Area") {
    AreaChart(
      renderer = AreaChartRenderer(
        AreaChartData(
          labels = listOf("A", "B", "C", "D", "E", "F"),
          values = listOf(12f, 28f, 18f, 34f, 24f, 40f),
          color = palette[0],
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Step Line") {
    StepLineChart(
      renderer = StepLineChartRenderer(
        StepLineChartData(
          labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri"),
          values = listOf(20f, 35f, 30f, 45f, 38f),
          color = palette[1],
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Candlestick") {
    CandlestickChart(
      renderer = CandlestickChartRenderer(
        CandlestickData(
          candles = listOf(
            Candle("1", open = 20f, high = 30f, low = 16f, close = 26f),
            Candle("2", open = 26f, high = 32f, low = 22f, close = 23f),
            Candle("3", open = 23f, high = 28f, low = 18f, close = 27f),
            Candle("4", open = 27f, high = 38f, low = 25f, close = 35f),
            Candle("5", open = 35f, high = 37f, low = 28f, close = 30f),
            Candle("6", open = 30f, high = 40f, low = 29f, close = 39f),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("K-Line") {
    CandlestickChart(
      renderer = CandlestickChartRenderer(
        CandlestickData(
          candles = listOf(
            Candle("Mon", open = 42f, high = 48f, low = 40f, close = 45f),
            Candle("Tue", open = 45f, high = 46f, low = 38f, close = 39f),
            Candle("Wed", open = 39f, high = 44f, low = 37f, close = 43f),
            Candle("Thu", open = 43f, high = 52f, low = 42f, close = 50f),
            Candle("Fri", open = 50f, high = 51f, low = 44f, close = 46f),
            Candle("Sat", open = 46f, high = 49f, low = 41f, close = 48f),
            Candle("Sun", open = 48f, high = 55f, low = 47f, close = 54f),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Bullet") {
    BulletChart(
      renderer = BulletChartRenderer(
        BulletData(
          metrics = listOf(
            BulletMetric(
              "Revenue",
              value = 72f,
              target = 80f,
              ranges = listOf(40f, 65f, 100f),
              color = palette[7],
            ),
            BulletMetric(
              "Profit",
              value = 55f,
              target = 50f,
              ranges = listOf(30f, 60f, 90f),
              color = palette[1],
            ),
            BulletMetric(
              "Users",
              value = 88f,
              target = 75f,
              ranges = listOf(50f, 70f, 100f),
              color = palette[2],
            ),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Box Plot") {
    BoxPlotChart(
      renderer = BoxPlotChartRenderer(
        BoxPlotData(
          groups = listOf(
            BoxGroup(
              "A",
              min = 5f,
              q1 = 18f,
              median = 28f,
              q3 = 38f,
              max = 52f,
              color = palette[2],
            ),
            BoxGroup(
              "B",
              min = 10f,
              q1 = 22f,
              median = 30f,
              q3 = 41f,
              max = 48f,
              color = palette[0],
            ),
            BoxGroup(
              "C",
              min = 8f,
              q1 = 15f,
              median = 24f,
              q3 = 33f,
              max = 44f,
              color = palette[1],
            ),
            BoxGroup(
              "D",
              min = 12f,
              q1 = 26f,
              median = 35f,
              q3 = 45f,
              max = 58f,
              color = palette[4],
            ),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Funnel") {
    FunnelChart(
      renderer = FunnelChartRenderer(
        FunnelData(
          stages = listOf(
            FunnelStage("Visits", 100f, palette[0]),
            FunnelStage("Signups", 64f, palette[1]),
            FunnelStage("Trials", 38f, palette[2]),
            FunnelStage("Paid", 18f, palette[3]),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Gauge") {
    GaugeChart(
      renderer = GaugeChartRenderer(
        GaugeData(value = 72f, min = 0f, max = 100f, label = "Score", color = palette[1]),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Treemap") {
    TreemapChart(
      renderer = TreemapChartRenderer(
        TreemapData(
          items = listOf(
            TreemapItem("Mobile", 45f, palette[0]),
            TreemapItem("Desktop", 30f, palette[1]),
            TreemapItem("Tablet", 15f, palette[2]),
            TreemapItem("Watch", 8f, palette[3]),
            TreemapItem("TV", 5f, palette[4]),
            TreemapItem("Other", 3f, palette[5]),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Polar Area") {
    PolarAreaChart(
      renderer = PolarAreaChartRenderer(
        PolarAreaData(
          slices = listOf(
            PolarSlice("N", 40f, palette[0]),
            PolarSlice("NE", 28f, palette[1]),
            PolarSlice("E", 35f, palette[2]),
            PolarSlice("SE", 22f, palette[3]),
            PolarSlice("S", 30f, palette[4]),
            PolarSlice("SW", 18f, palette[6]),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Sunburst") {
    SunburstChart(
      renderer = SunburstChartRenderer(
        SunburstData(
          roots = listOf(
            SunburstNode(
              "Web",
              50f,
              palette[0],
              children = listOf(
                SunburstNode("HTML", 20f, palette[0]),
                SunburstNode("CSS", 15f, palette[0]),
                SunburstNode("JS", 15f, palette[0]),
              ),
            ),
            SunburstNode(
              "Mobile",
              35f,
              palette[1],
              children = listOf(
                SunburstNode("iOS", 20f, palette[1]),
                SunburstNode("Android", 15f, palette[1]),
              ),
            ),
            SunburstNode(
              "Backend",
              25f,
              palette[2],
              children = listOf(
                SunburstNode("API", 15f, palette[2]),
                SunburstNode("DB", 10f, palette[2]),
              ),
            ),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Sankey") {
    SankeyChart(
      renderer = SankeyChartRenderer(
        SankeyData(
          nodes = listOf(
            SankeyNode("a", "Source A", column = 0, color = palette[0]),
            SankeyNode("b", "Source B", column = 0, color = palette[1]),
            SankeyNode("m", "Hub", column = 1, color = palette[2]),
            SankeyNode("x", "Out X", column = 2, color = palette[3]),
            SankeyNode("y", "Out Y", column = 2, color = palette[4]),
          ),
          links = listOf(
            SankeyLink("a", "m", 30f),
            SankeyLink("b", "m", 20f),
            SankeyLink("m", "x", 28f),
            SankeyLink("m", "y", 22f),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
  ChartEntry("Stream Graph") {
    StreamGraphChart(
      renderer = StreamGraphChartRenderer(
        StreamData(
          labels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
          series = listOf(
            StreamSeries("A", listOf(10f, 14f, 12f, 18f, 16f, 22f), palette[0]),
            StreamSeries("B", listOf(8f, 10f, 16f, 12f, 18f, 14f), palette[1]),
            StreamSeries("C", listOf(6f, 9f, 8f, 14f, 11f, 16f), palette[2]),
            StreamSeries("D", listOf(4f, 6f, 10f, 7f, 12f, 9f), palette[4]),
          ),
        ),
      ),
      modifier = chartModifier(),
      isSystemInDarkTheme = DARK,
      animate = true,
    )
  },
)

private fun chartModifier(): Modifier = Modifier.height(190.dp).fillMaxWidth()

@Composable
private fun ChartCard(index: Int, replayKey: Int, entry: ChartEntry) {
  // Staggered entrance: fade + scale in, offset by grid index.
  var appeared by remember(replayKey) { mutableStateOf(false) }
  LaunchedEffect(replayKey) { appeared = true }
  // Keep as State (no `by`) and read it inside graphicsLayer so the entrance animation
  // runs in the draw/layer phase — it never recomposes the chart subtree per frame.
  val progress = animateFloatAsState(
    targetValue = if (appeared) 1f else 0f,
    animationSpec = tween(durationMillis = 450, delayMillis = (index % 12) * 60),
    label = "appear",
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .graphicsLayer {
        val p = progress.value
        alpha = p
        scaleX = 0.94f + 0.06f * p
        scaleY = 0.94f + 0.06f * p
      },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = CardColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(
        text = entry.title,
        color = TitleColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp),
      )
      // key(replayKey) recreates the chart so its entry animation replays.
      key(replayKey) { entry.content() }
    }
  }
}

@Composable
public fun ChartShowcase(): Unit = MaterialTheme(colorScheme = lightColorScheme()) {
  var replayKey by remember { mutableStateOf(0) }
  val entries = remember { chartEntries() }

  Surface(modifier = Modifier.fillMaxSize(), color = SurfaceColor) {
    Column(modifier = Modifier.fillMaxSize()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 28.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text(
            text = "Drafter — Desktop Showcase",
            color = TitleColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "${entries.size} charts · premium styling · animated",
            color = SubtitleColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp),
          )
        }
        FilledTonalButton(onClick = { replayKey++ }) {
          Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.height(18.dp))
          Text("  Replay animations")
        }
      }

      LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize(),
      ) {
        itemsIndexed(entries) { index, entry ->
          ChartCard(index = index, replayKey = replayKey, entry = entry)
        }
      }
    }
  }
}
