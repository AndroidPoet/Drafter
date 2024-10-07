<h1 align="center">Drafter</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/androidpoet/Drafter/actions/workflows/android.yml"><img alt="Build Status" 
  src="https://github.com/androidpoet/Drafter/actions/workflows/android.yml/badge.svg"/></a>
</p>

<p align="center">
📊 A powerful, flexible charting library for Compose Multiplatform applications
</p>


<p align="center">
  <img src="https://github.com/user-attachments/assets/c61658e4-cb22-40ae-8050-3bb6f300e3c4" alt="Drafter Demo" width="300"/>
</p>


<p align="center">
  <img src="https://github.com/user-attachments/assets/25e678d0-6276-49f9-b38d-f9fa8c5a75fb" alt="Drafter Desktop Demo" width="900"/>
</p>



## Features

- 📊 Supports multiple chart types (Bar, Line, Pie, Scatter, Histogram, Waterfall)
- 🎨 Highly customizable appearance
- 🚀 Efficient rendering for smooth animations
- 📱 Responsive design for various screen sizes
- 🖥️ Multiplatform support (Android, iOS, Desktop)

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

## Bar Charts

### Simple Bar Chart

```kotlin
val simpleData = SimpleBarChartData(
  labels = listOf("A", "B", "C", "D"),
  values = listOf(10f, 20f, 15f, 25f),
  colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
)
val simpleRenderer = SimpleBarChartRenderer()

BarChart(
  data = simpleData,
  renderer = simpleRenderer,
  modifier = Modifier
    .fillMaxWidth()
    .height(200.dp)
    .padding(horizontal = 16.dp)
)
```

### Grouped Bar Chart

```kotlin
val groupedData = GroupedBarChartData(
  labels = listOf("Q1", "Q2", "Q3", "Q4"),
  itemNames = listOf("Product A", "Product B"),
  groupedValues = listOf(
    listOf(10f, 15f),
    listOf(20f, 25f),
    listOf(15f, 10f),
    listOf(25f, 20f)
  ),
  colors = listOf(Color.Cyan, Color.Magenta)
)
val groupedRenderer = GroupedBarChartRenderer()

BarChart(
  data = groupedData,
  renderer = groupedRenderer,
  modifier = Modifier
    .fillMaxWidth()
    .height(200.dp)
    .padding(horizontal = 16.dp)
)
```

### Stacked Bar Chart

```kotlin
val stackedData = StackedBarChartData(
  labels = listOf("Jan", "Feb", "Mar", "Apr"),
  stacks = listOf(
    listOf(5f, 5f, 2f),
    listOf(7f, 3f, 4f),
    listOf(6f, 4f, 3f),
    listOf(8f, 2f, 5f)
  ),
  colors = listOf(Color.Blue, Color.Red, Color.Green)
)
val stackedRenderer = StackedBarChartRenderer()

BarChart(
  data = stackedData,
  renderer = stackedRenderer,
  modifier = Modifier
    .fillMaxWidth()
    .height(200.dp)
    .padding(horizontal = 16.dp)
)
```

## Line Charts

### Simple Line Chart

```kotlin
val simpleData = SimpleLineChartData(
  labels = listOf("A", "B", "C", "D"),
  values = listOf(10f, 20f, 15f, 25f),
  color = Color.Blue
)
val simpleRenderer = SimpleLineChartRenderer()

LineChart(
  data = simpleData,
  renderer = simpleRenderer,
  modifier = Modifier
    .fillMaxWidth()
    .height(200.dp)
    .padding(horizontal = 16.dp)
)
```

### Grouped Line Chart

```kotlin
val groupedData = GroupedLineChartData(
  labels = listOf("Q1", "Q2", "Q3", "Q4"),
  itemNames = listOf("Product A", "Product B"),
  groupedValues = listOf(
    listOf(10f, 15f),
    listOf(20f, 25f),
    listOf(15f, 10f),
    listOf(25f, 20f)
  ),
  colors = listOf(Color.Cyan, Color.Magenta)
)
val groupedRenderer = GroupedLineChartRenderer()

LineChart(
  data = groupedData,
  renderer = groupedRenderer,
  modifier = Modifier
    .fillMaxWidth()
    .height(200.dp)
    .padding(horizontal = 16.dp)
)
```

### Stacked Line Chart (Area Chart)

```kotlin
val stackedData = StackedLineChartData(
  labels = listOf("Jan", "Feb", "Mar", "Apr"),
  stacks = listOf(
    listOf(5f, 5f, 2f),
    listOf(7f, 3f, 4f),
    listOf(6f, 4f, 3f),
    listOf(8f, 2f, 5f)
  ),
  colors = listOf(Color.Blue, Color.Red, Color.Green)
)
val stackedRenderer = StackedLineChartRenderer()

LineChart(
  data = stackedData,
  renderer = stackedRenderer,
  modifier = Modifier
    .fillMaxWidth()
    .height(200.dp)
    .padding(horizontal = 16.dp)
)
```

## Histogram Chart

```kotlin
val dataPoints = listOf(1f, 2f, 2f, 3f, 3f, 3f, 4f, 4f, 5f, 5f, 5f, 5f)
val binCount = 5

HistogramChart(
  dataPoints = dataPoints,
  binCount = binCount,
  color = Color.Magenta,
  modifier = Modifier
    .fillMaxWidth()
    .height(200.dp)
    .padding(horizontal = 16.dp)
)
```

## Pie Chart

```kotlin
val pieData = PieChartData(
  slices = listOf(
    PieChartData.Slice(40f, Color.Blue, "Blue"),
    PieChartData.Slice(30f, Color.Red, "Red"),
    PieChartData.Slice(20f, Color.Green, "Green"),
    PieChartData.Slice(10f, Color.Yellow, "Yellow")
  )
)

PieChart(
  data = pieData,
  modifier = Modifier.size(200.dp)
)
```

## Scatter Plot Chart

```kotlin
val numberOfPoints = 30
val randomPoints = List(numberOfPoints) {
  Pair(
    Random.nextFloat() * 50f,
    Random.nextFloat() * 50f
  )
}
val randomColors = List(numberOfPoints) {
  Color(
    red = Random.nextFloat(),
    green = Random.nextFloat(),
    blue = Random.nextFloat(),
    alpha = 1f
  )
}
val data = ScatterPlotData(
  points = randomPoints,
  pointColors = randomColors
)
val renderer = SimpleScatterPlotRenderer()

ScatterPlot(
  data = data,
  renderer = renderer,
  modifier = Modifier.size(300.dp)
)
```

## Waterfall Chart

```kotlin
val data = WaterfallChartData(
  labels = listOf("Q1", "Q2", "Q3", "Q4"),
  values = listOf(500f, -200f, 300f, -100f),
  colors = listOf(Color.Green, Color.Red, Color.Green, Color.Red),
  initialValue = 1000f
)
val renderer = WaterfallChartRenderer()

BarChart(
  data = data,
  renderer = renderer,
  modifier = Modifier.size(400.dp, 300.dp)
)
```

## Radar Chart

```kotlin
@Composable
fun RadarChartExample() {
  ChartTitle(text = "Radar Chart")
  val data = listOf(
    RadarChartData(
      mapOf(
        "Execution" to 0.8f,
        "Landing" to 0.6f,
        "Difficulty" to 0.9f,
        "Style" to 0.7f,
        "Creativity" to 0.85f,
      ),
    ),
  )
  val colors = listOf(Color.Blue, Color.Red)

  RadarChart(
    data = data,
    colors = colors,
  )
}
```

## Gantt Chart

```kotlin
@Composable
fun GanttChartExample() {
  ChartTitle(text = "Gantt Chart")
  val tasks = listOf(
    GanttTask("Planning", 0f, 2f),
    GanttTask("Design", 2f, 2f),
    GanttTask("Development", 4f, 3f),
    GanttTask("Testing", 7f, 2f),
    GanttTask("Deployment", 9f, 1f),
  )
  val data = GanttChartData(tasks)
  val renderer = SimpleGanttChartRenderer()
  GanttChart(data = data, renderer = renderer, modifier = Modifier.size(400.dp))
}
```

## Bubble Chart

```kotlin
@Composable
fun BubbleChartExample() {
  ChartTitle(text = "Bubble Chart")
  val data = listOf(
    listOf(
      BubbleChartData(10f, 26f, 30f, Color.Blue),
      BubbleChartData(26f, 30f, 60f, Color.Blue),
      BubbleChartData(26f, 46f, 45f, Color.Blue),
    ),
    listOf(
      BubbleChartData(14f, 15f, 30f, Color.Green),
      BubbleChartData(22f, 36f, 45f, Color.Green),
      BubbleChartData(40f, 57f, 75f, Color.Green),
    ),
    listOf(
      BubbleChartData(8f, 9f, 30f, Color.Yellow),
      BubbleChartData(20f, 57f, 45f, Color.Yellow),
      BubbleChartData(40f, 50f, 60f, Color.Yellow),
    ),
    listOf(
      BubbleChartData(8f, 20f, 22.5f, Color.Red),
      BubbleChartData(12f, 30f, 30f, Color.Red),
      BubbleChartData(30f, 40f, 45f, Color.Red),
    )
  )

  BubbleChart(data, Modifier.size(300.dp))
}
```

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
