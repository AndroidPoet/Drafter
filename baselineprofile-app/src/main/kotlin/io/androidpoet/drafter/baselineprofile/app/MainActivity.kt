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
package io.androidpoet.drafter.baselineprofile.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.baselineprofile.app.bars.GroupedBarChartExample
import io.androidpoet.drafter.baselineprofile.app.bars.HistogramChartExample
import io.androidpoet.drafter.baselineprofile.app.bars.SimpleBarChartExample
import io.androidpoet.drafter.baselineprofile.app.bars.StackedBarChartExample
import io.androidpoet.drafter.baselineprofile.app.bars.WaterfallChartExample
import io.androidpoet.drafter.baselineprofile.app.buble.BubbleChartExample
import io.androidpoet.drafter.baselineprofile.app.gantt.GanttChartExample
import io.androidpoet.drafter.baselineprofile.app.githubgraph.GithubGraph
import io.androidpoet.drafter.baselineprofile.app.line.GroupedLineChartExample
import io.androidpoet.drafter.baselineprofile.app.line.ScatterPlotChartExample
import io.androidpoet.drafter.baselineprofile.app.line.SimpleLineChartExample
import io.androidpoet.drafter.baselineprofile.app.line.StackedLineChartExample
import io.androidpoet.drafter.baselineprofile.app.manager.ChartThemeManager
import io.androidpoet.drafter.baselineprofile.app.pie.DonutChartExample
import io.androidpoet.drafter.baselineprofile.app.pie.PieChartExample
import io.androidpoet.drafter.baselineprofile.app.radar.RadarChartExample
import io.androidpoet.drafter.baselineprofile.app.ui.theme.DrafterDemoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      DrafterDemoTheme {
        val currentTheme by remember { ChartThemeManager.currentTheme }
        val palette by remember { ChartThemeManager.palette }
        Surface(modifier = Modifier.background(Color.White)) {
          LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(16.dp),
            state = rememberLazyGridState(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
          ) {
            item {
              Column {
                ChartTitle(text = "Simple Bar Chart")
                SimpleBarChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Grouped Bar Chart")
                GroupedBarChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Stacked Bar Chart")
                StackedBarChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Simple Line Chart")
                SimpleLineChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Grouped Line Chart")
                GroupedLineChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Stacked Line Chart")
                StackedLineChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Histogram Chart")
                HistogramChartExample()
              }
            }
            item {
              Column {
                ChartTitle(text = "Pie Chart")
                PieChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Donut Chart")
                DonutChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Scatter Plot Chart")
                ScatterPlotChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Waterfall Chart")
                WaterfallChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Radar Chart")
                RadarChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Gantt Chart")
                GanttChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Bubble Chart")
                BubbleChartExample(colors = palette)
              }
            }
            item {
              Column {
                ChartTitle(text = "Github Contribution Graph")
                GithubGraph(color = Color(0xFF03DAC5))
              }
            }
          }
        }
      }
    }
  }
}
