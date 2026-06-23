<h1 align="center">Drafter</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/androidpoet/Drafter/actions/workflows/android.yml"><img alt="Build Status" 
  src="https://github.com/androidpoet/Drafter/actions/workflows/android.yml/badge.svg"/></a>
  
</p>

<div align="center">

<p align="center">
📊 A powerful, flexible charting library for Compose Multiplatform applications


</p>

  [Web App Demo](https://androidpoet.github.io/drafterdemo/)
</div>




<p align="center">
  <img src="https://github.com/user-attachments/assets/c61658e4-cb22-40ae-8050-3bb6f300e3c4" alt="Drafter Demo" width="300"/>
</p>


<p align="center">
  <img src="https://github.com/user-attachments/assets/25e678d0-6276-49f9-b38d-f9fa8c5a75fb" alt="Drafter Desktop Demo" width="900"/>
</p>



## Features

- 📊 **27 chart types** out of the box:
  - **Bars** — Bar, Grouped Bar, Stacked Bar, Histogram, Waterfall
  - **Lines** — Line, Grouped Line, Stacked Line, Step Line, Area
  - **Distribution** — Scatter, Bubble, Box Plot, Candlestick
  - **Part-to-whole** — Pie, Donut, Funnel, Treemap, Polar Area, Sunburst
  - **Specialized** — Radar, Gantt, Gauge, Bullet, Sankey, Stream Graph, Contribution Heatmap
- 🎨 Highly customizable appearance with a shared `DrafterTheme` (light/dark, custom palettes)
- 🚀 Efficient, recomposition-friendly rendering (`@Immutable` data, layer-phase animations)
- 📱 Responsive design for various screen sizes
- 🖥️ Multiplatform support (Android, iOS, Desktop/JVM, Web/Wasm, JS, macOS)

## Download
[![Maven Central](https://img.shields.io/maven-central/v/io.github.androidpoet/drafter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.androidpoet%22%20AND%20a:%22drafter%22)

### Gradle

Add the dependency below to your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("io.github.androidpoet:drafter:$drafter_version")
}
```

For Kotlin Multiplatform, add the dependency below to your **module**'s `build.gradle.kts` file:

```kotlin
sourceSets {
  val commonMain by getting {
    dependencies {
      implementation("io.github.androidpoet:drafter:$drafter_version")
    }
  }
}
```

## Table of Contents

1. [Bar Charts](#bar-charts)
  - [Simple Bar Chart](#simple-bar-chart)
  - [Grouped Bar Chart](#grouped-bar-chart)
  - [Stacked Bar Chart](#stacked-bar-chart)
2. [Line Charts](#line-charts)
  - [Simple Line Chart](#simple-line-chart)
  - [Grouped Line Chart](#grouped-line-chart)
  - [Stacked Line Chart (Area Chart)](#stacked-line-chart-area-chart)
3. [Histogram Chart](#histogram-chart)
4. [Pie Chart](#pie-chart)
5. [Scatter Plot Chart](#scatter-plot-chart)
6. [Waterfall Chart](#waterfall-chart)
7. [Radar Chart](#radar-chart)
8. [Gantt Chart](#gantt-chart)
9. [Bubble Chart](#bubble-chart)
10. [HeatMap Chart](#heatmap-chart)
11. [Area Chart](#area-chart)
12. [Step Line Chart](#step-line-chart)
13. [Candlestick Chart](#candlestick-chart)
14. [Box Plot Chart](#box-plot-chart)
15. [Bullet Chart](#bullet-chart)
16. [Funnel Chart](#funnel-chart)
17. [Gauge Chart](#gauge-chart)
18. [Treemap Chart](#treemap-chart)
19. [Polar Area Chart](#polar-area-chart)
20. [Sunburst Chart](#sunburst-chart)
21. [Sankey Chart](#sankey-chart)
22. [Stream Graph Chart](#stream-graph-chart)

## Bar Charts

### Simple Bar Chart

```kotlin
private fun getBarChartData(colors: List<Color>) =
  SimpleBarChartData(
    labelsList = listOf("Jan", "Feb", "Mar", "Apr"),
    values = listOf(10f, 30f, 15f, 45f),
    colors = colors,
  )

private fun getSimpleBarChartRenderer(colors: List<Color>) =
  BarChartRenderer(getBarChartData(colors = colors))

@Composable
fun SimpleBarChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  BarChart(
    renderer = getSimpleBarChartRenderer(colors = colors),
    modifier =
    modifier
      .height(300.dp)
      .fillMaxWidth(),
    animate = true,
  )
}

```

### Grouped Bar Chart

```kotlin
private fun getBarChartRenderer() =
  GroupedBarChartRenderer(
    GroupedBarChartData(
      labelsList = listOf("2020", "2021", "2022"),
      itemNames = listOf("Product A", "Product B", "Product C"),
      groupedValues =
      listOf(
        listOf(10f, 20f, 15f), // 2020
        listOf(25f, 5f, 30f), // 2021
        listOf(12f, 28f, 10f), // 2022
      ),
      colors = listOf(Color.Red, Color.Green, Color.Blue),
    ),
  )

@Composable
fun GroupedBarChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  BarChart(
    renderer = getBarChartRenderer(),
    modifier =
    Modifier
      .height(300.dp)
      .fillMaxWidth(),
    animate = true,
  )
}
```

### Stacked Bar Chart

```kotlin
private fun getStackedBarChartData(colors: List<Color>) =
  StackedBarChartData(
    labelsList = listOf("Q1", "Q2", "Q3"),
    stacks =
    listOf(
      listOf(10f, 15f, 5f), // Q1
      listOf(8f, 12f, 20f), // Q2
      listOf(18f, 10f, 15f), // Q3
    ),
    colors = colors,
  )

private fun getStackedBarChartRenderer(colors: List<Color>) =
  StackedBarChartRenderer(getStackedBarChartData(colors = colors))

@Composable
fun StackedBarChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  BarChart(
    renderer = getStackedBarChartRenderer(colors = colors),
    modifier =
    modifier
      .height(300.dp)
      .fillMaxWidth(),
    animate = true,
  )
}
```

## Line Charts

### Simple Line Chart

```kotlin
private fun getLineChartRenderer(colors: List<Color>) =
  LineChartRenderer(
    SimpleLineChartData(
      labels = listOf("A", "B", "C", "D"),
      values = listOf(10f, 20f, 15f, 25f),
      color = colors.first(),
    ),
  )

@Composable
fun SimpleLineChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  LineChart(
    renderer = getLineChartRenderer(colors = colors),
    modifier = modifier.fillMaxSize(),
  )
}
```

### Grouped Line Chart

```kotlin
private fun getGroupedLineChartData(colors: List<Color>) =
  GroupedLineChartData(
    labels = listOf("Q1", "Q2", "Q3", "Q4"),
    itemNames = listOf("Product A", "Product B"),
    groupedValues =
    listOf(
      listOf(10f, 15f),
      listOf(20f, 25f),
      listOf(15f, 10f),
      listOf(25f, 20f),
    ),
    colors = colors,
  )

private fun getGroupedLineChartRenderer(colors: List<Color>) =
  GroupedLineChartRenderer(getGroupedLineChartData(colors = colors))

@Composable
fun GroupedLineChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  ChartContainer {
    LineChart(
      renderer = getGroupedLineChartRenderer(colors = colors),
      modifier = modifier.fillMaxSize(),
    )
  }
}
```

### Stacked Line Chart (Area Chart)

```kotlin
private fun getStackedLineChartRenderer(colors: List<Color>) =
  StackedLineChartRenderer(
    StackedLineChartData(
      labels = listOf("Jan", "Feb", "Mar", "Apr"),
      stacks =
      listOf(
        listOf(5f, 5f, 2f),
        listOf(7f, 3f, 4f),
        listOf(6f, 4f, 3f),
        listOf(8f, 2f, 5f),
      ),
      colors = colors,
    ),
  )

@Composable
fun StackedLineChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  ChartContainer {
    LineChart(
      renderer = getStackedLineChartRenderer(colors = colors),
      modifier = Modifier.fillMaxSize(),
    )
  }
}
```

## Histogram Chart

```kotlin
private fun getHistogramData() = listOf(1f, 2f, 2f, 3f, 3f, 3f, 4f, 4f, 5f, 5f, 5f, 5f)

private fun getHistogramRenderer() =
  HistogramRenderer(
    dataPoints = getHistogramData(),
    binCount = 5,
    color = Color.Blue,
  )

@Composable
fun HistogramChartExample(
  modifier: Modifier = Modifier,
  animate: Boolean = true,
) {
  BarChart(
    renderer = getHistogramRenderer(),
    modifier = modifier.size(300.dp),
    animate = animate,
  )
}
```

## Pie Chart

```kotlin
private fun getPieChartRenderer(colors: List<Color>) =
  PieChartRenderer(
    PieChartData(
      slices =
      listOf(
        PieChartData.Slice(value = 40f, color = colors[0], label = "Red"),
        PieChartData.Slice(value = 30f, color = colors[1], label = "Green"),
        PieChartData.Slice(value = 20f, color = colors[2], label = "Blue"),
        PieChartData.Slice(value = 10f, color = colors[3], label = "Purple"),
      ),
    ),
  )

private fun getDonutPieChartRenderer(colors: List<Color>) =
  DonutChartRenderer(
    PieChartData(
      slices =
      listOf(
        PieChartData.Slice(value = 40f, color = colors[0], label = "Red"),
        PieChartData.Slice(value = 30f, color = colors[1], label = "Green"),
        PieChartData.Slice(value = 20f, color = colors[2], label = "Blue"),
        PieChartData.Slice(value = 10f, color = colors[3], label = "Purple"),
      ),
    ),
  )

@Composable
fun PieChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  PieChart(
    renderer = getPieChartRenderer(colors = colors),
    modifier = Modifier.size(200.dp),
    animate = true,
  )
}

@Composable
fun DonutChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  PieChart(
    renderer = getDonutPieChartRenderer(colors = colors),
    modifier = Modifier.size(200.dp),
    animate = true,
  )
}
```

## Scatter Plot Chart

```kotlin
private fun getScatterPlotRenderer(colors: List<Color>) =
  SimpleScatterPlotRenderer(
    ScatterPlotData(
      points =
      List(30) {
        Pair(
          Random.nextFloat() * 50f,
          Random.nextFloat() * 50f,
        )
      },
      pointColors =
      List(30) {
        if (colors.isNotEmpty()) colors[it % colors.size] else Color.Gray
      },
    ),
  )

@Composable
fun ScatterPlotChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  ScatterPlot(
    modifier = Modifier.size(300.dp),
    renderer = getScatterPlotRenderer(colors = colors),
  )
}
```

## Waterfall Chart

```kotlin
private fun getWaterfallChartRenderer(colors: List<Color>) =
  WaterfallChartRenderer(
    WaterfallChartData(
      labelsList = listOf("Start", "Revenue", "Cost", "Profit"),
      values = listOf(+50f, -20f, +30f), // Changes from 'Start'
      colors = colors,
      initialValue = 100f, // Start from 100
    ),
  )

@Composable
fun WaterfallChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  BarChart(
    renderer = getWaterfallChartRenderer(colors = colors),
    modifier =
    modifier
      .height(300.dp)
      .fillMaxWidth(),
    animate = true,
  )
}
```

## Radar Chart

```kotlin
private fun getRadarRenderer(colors: List<Color>) =
  RadarChartRenderer(
    data =
    listOf(
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
    colors = colors,
  )

@Composable
fun RadarChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  RadarChart(
    modifier = modifier.size(300.dp),
    renderer = getRadarRenderer(colors),
    isSystemInDarkTheme = isSystemInDarkTheme()
  )
}
```

## Gantt Chart

```kotlin
private fun getGanttChartRenderer(colors: List<Color>) =
  GanttChartRenderer(
    GanttChartData(
      taskColors = colors,
      tasks =
      listOf(
        GanttTask("Planning", 0f, 2f),
        GanttTask("Design", 2f, 2f),
        GanttTask("Development", 4f, 3f),
        GanttTask("Testing", 7f, 2f),
        GanttTask("Deployment", 9f, 1f),
      ),
    ),
  )

@Composable
fun GanttChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  GanttChart(
    renderer = getGanttChartRenderer(colors = colors),
    modifier = modifier.size(400.dp),
  )
}
```

## Bubble Chart

```kotlin
private fun getBubbleChartData(colors: List<Color>) =
  BubbleChartData(
    series =
    listOf(
      listOf(
        BubbleChartData.BubbleData(10f, 26f, 30f, colors[0]),
        BubbleChartData.BubbleData(26f, 30f, 60f, colors[0]),
        BubbleChartData.BubbleData(26f, 46f, 45f, colors[0]),
      ),
      listOf(
        BubbleChartData.BubbleData(14f, 15f, 30f, colors[1]),
        BubbleChartData.BubbleData(22f, 36f, 45f, colors[1]),
        BubbleChartData.BubbleData(90f, 57f, 75f, colors[1]),
      ),
      listOf(
        BubbleChartData.BubbleData(8f, 9f, 90f, colors[2]),
        BubbleChartData.BubbleData(20f, 57f, 45f, colors[2]),
        BubbleChartData.BubbleData(40f, 50f, 60f, colors[2]),
      ),
      listOf(
        BubbleChartData.BubbleData(8f, 20f, 22.5f, colors[3]),
        BubbleChartData.BubbleData(12f, 30f, 30f, colors[3]),
        BubbleChartData.BubbleData(30f, 40f, 45f, colors[3]),
      ),
    ),
  )

private fun getBubbleChartRenderer(colors: List<Color>) =
  SimpleBubbleChartDataRenderer(getBubbleChartData(colors = colors))

@Composable
fun BubbleChartExample(
  colors: List<Color>,
  modifier: Modifier = Modifier,
) {
  BubbleChart(
    renderer = getBubbleChartRenderer(colors = colors),
    modifier = modifier.size(300.dp),
  )
}
```

## HeatMap Chart

```kotlin
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
  modifier: Modifier = Modifier,
  color: Color,
) {
  Heatmap(
    renderer = getHeatmapRenderer(color = color),
    modifier =
    Modifier
      .fillMaxWidth()
      .height(112.dp),
  )
}
```

## Area Chart

```kotlin
@Composable
fun AreaChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  AreaChart(
    renderer = AreaChartRenderer(
      AreaChartData(
        labels = listOf("A", "B", "C", "D", "E", "F"),
        values = listOf(12f, 28f, 18f, 34f, 24f, 40f),
        color = colors[0],
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Step Line Chart

```kotlin
@Composable
fun StepLineChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  StepLineChart(
    renderer = StepLineChartRenderer(
      StepLineChartData(
        labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri"),
        values = listOf(20f, 35f, 30f, 45f, 38f),
        color = colors[1],
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Candlestick Chart

```kotlin
@Composable
fun CandlestickChartExample(modifier: Modifier = Modifier) {
  CandlestickChart(
    renderer = CandlestickChartRenderer(
      CandlestickData(
        candles = listOf(
          Candle("1", open = 20f, high = 30f, low = 16f, close = 26f),
          Candle("2", open = 26f, high = 32f, low = 22f, close = 23f),
          Candle("3", open = 23f, high = 28f, low = 18f, close = 27f),
          Candle("4", open = 27f, high = 38f, low = 25f, close = 35f),
        ),
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Box Plot Chart

```kotlin
@Composable
fun BoxPlotChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  BoxPlotChart(
    renderer = BoxPlotChartRenderer(
      BoxPlotData(
        groups = listOf(
          BoxGroup("A", min = 5f, q1 = 18f, median = 28f, q3 = 38f, max = 52f, color = colors[2]),
          BoxGroup("B", min = 10f, q1 = 22f, median = 30f, q3 = 41f, max = 48f, color = colors[0]),
          BoxGroup("C", min = 8f, q1 = 15f, median = 24f, q3 = 33f, max = 44f, color = colors[1]),
        ),
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Bullet Chart

```kotlin
@Composable
fun BulletChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  BulletChart(
    renderer = BulletChartRenderer(
      BulletData(
        metrics = listOf(
          BulletMetric("Revenue", value = 72f, target = 80f, ranges = listOf(40f, 65f, 100f), color = colors[0]),
          BulletMetric("Profit", value = 55f, target = 50f, ranges = listOf(30f, 60f, 90f), color = colors[1]),
        ),
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Funnel Chart

```kotlin
@Composable
fun FunnelChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  FunnelChart(
    renderer = FunnelChartRenderer(
      FunnelData(
        stages = listOf(
          FunnelStage("Visits", 100f, colors[0]),
          FunnelStage("Signups", 64f, colors[1]),
          FunnelStage("Trials", 38f, colors[2]),
          FunnelStage("Paid", 18f, colors[3]),
        ),
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Gauge Chart

```kotlin
@Composable
fun GaugeChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  GaugeChart(
    renderer = GaugeChartRenderer(
      GaugeData(value = 72f, min = 0f, max = 100f, label = "Score", color = colors[1]),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Treemap Chart

```kotlin
@Composable
fun TreemapChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  TreemapChart(
    renderer = TreemapChartRenderer(
      TreemapData(
        items = listOf(
          TreemapItem("Mobile", 45f, colors[0]),
          TreemapItem("Desktop", 30f, colors[1]),
          TreemapItem("Tablet", 15f, colors[2]),
          TreemapItem("Watch", 8f, colors[3]),
        ),
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Polar Area Chart

```kotlin
@Composable
fun PolarAreaChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  PolarAreaChart(
    renderer = PolarAreaChartRenderer(
      PolarAreaData(
        slices = listOf(
          PolarSlice("N", 40f, colors[0]),
          PolarSlice("E", 35f, colors[2]),
          PolarSlice("S", 30f, colors[4]),
          PolarSlice("W", 22f, colors[3]),
        ),
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Sunburst Chart

```kotlin
@Composable
fun SunburstChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  SunburstChart(
    renderer = SunburstChartRenderer(
      SunburstData(
        roots = listOf(
          SunburstNode(
            "Web", 50f, colors[0],
            children = listOf(
              SunburstNode("HTML", 20f, colors[0]),
              SunburstNode("CSS", 15f, colors[0]),
              SunburstNode("JS", 15f, colors[0]),
            ),
          ),
          SunburstNode(
            "Mobile", 35f, colors[1],
            children = listOf(
              SunburstNode("iOS", 20f, colors[1]),
              SunburstNode("Android", 15f, colors[1]),
            ),
          ),
        ),
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Sankey Chart

```kotlin
@Composable
fun SankeyChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  SankeyChart(
    renderer = SankeyChartRenderer(
      SankeyData(
        nodes = listOf(
          SankeyNode("a", "Source A", column = 0, color = colors[0]),
          SankeyNode("b", "Source B", column = 0, color = colors[1]),
          SankeyNode("m", "Hub", column = 1, color = colors[2]),
          SankeyNode("x", "Out X", column = 2, color = colors[3]),
          SankeyNode("y", "Out Y", column = 2, color = colors[4]),
        ),
        links = listOf(
          SankeyLink("a", "m", 30f),
          SankeyLink("b", "m", 20f),
          SankeyLink("m", "x", 28f),
          SankeyLink("m", "y", 22f),
        ),
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Stream Graph Chart

```kotlin
@Composable
fun StreamGraphChartExample(colors: List<Color>, modifier: Modifier = Modifier) {
  StreamGraphChart(
    renderer = StreamGraphChartRenderer(
      StreamData(
        labels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
        series = listOf(
          StreamSeries("A", listOf(10f, 14f, 12f, 18f, 16f, 22f), colors[0]),
          StreamSeries("B", listOf(8f, 10f, 16f, 12f, 18f, 14f), colors[1]),
          StreamSeries("C", listOf(6f, 9f, 8f, 14f, 11f, 16f), colors[2]),
        ),
      ),
    ),
    modifier = modifier.height(300.dp).fillMaxWidth(),
  )
}
```

## Theming

All charts read their palette and light/dark colors from `DrafterTheme`. Wrap your charts once to apply a consistent look (or supply a custom palette):

```kotlin
DrafterTheme(dark = isSystemInDarkTheme()) {
  // charts here pick up LocalDrafterTheme
  BarChart(renderer = renderer)
}
```

Every renderer also implements the shared `ChartRenderer` contract, so you can hold and pass them uniformly.

## Sample apps

- **Desktop (JVM):** `./gradlew :desktopApp:run` — a scrollable gallery of all 27 charts.
- **Web (Wasm):** `./gradlew :webApp:wasmJsBrowserRun` — the same gallery in the browser.

## Contributing

Contributions are welcome! If you've found a bug, have an idea for an improvement, or want to contribute new features, please open an issue or submit a pull request.

## Find this repository useful? :heart:
Support it by joining __[stargazers](https://github.com/androidpoet/drafter/stargazers)__ for this repository. :star: <br>
Also, __[follow me](https://github.com/androidpoet)__ on GitHub for my next creations! 🤩

## License
```
Designed and developed by AndroidPoet (Ranbir Singh)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
