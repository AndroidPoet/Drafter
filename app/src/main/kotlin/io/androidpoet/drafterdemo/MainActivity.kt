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
package io.androidpoet.drafterdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.androidpoet.drafterdemo.bars.GroupedBarChartExample
import io.androidpoet.drafterdemo.bars.HistogramChartExample
import io.androidpoet.drafterdemo.bars.SimpleBarChartExample
import io.androidpoet.drafterdemo.bars.StackedBarChartExample
import io.androidpoet.drafterdemo.bars.WaterfallChartExample
import io.androidpoet.drafterdemo.buble.BubbleChartExample
import io.androidpoet.drafterdemo.gantt.GanttChartExample
import io.androidpoet.drafterdemo.githubgraph.GithubGraph
import io.androidpoet.drafterdemo.line.GroupedLineChartExample
import io.androidpoet.drafterdemo.line.ScatterPlotChartExample
import io.androidpoet.drafterdemo.line.SimpleLineChartExample
import io.androidpoet.drafterdemo.line.StackedLineChartExample
import io.androidpoet.drafterdemo.manager.ChartThemeManager
import io.androidpoet.drafterdemo.pie.DonutChartExample
import io.androidpoet.drafterdemo.pie.PieChartExample
import io.androidpoet.drafterdemo.radar.RadarChartExample
import io.androidpoet.drafterdemo.ui.theme.DrafterDemoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      DrafterDemoTheme {
        val currentTheme by remember { ChartThemeManager.currentTheme }
        val palette by remember { ChartThemeManager.palette }

        Column(modifier = Modifier.fillMaxSize()) {
          // Theme Selector Row
          Row(
            modifier =
            Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            ChartThemeManager.ColorTheme.entries.forEach { theme ->
              Button(
                onClick = { ChartThemeManager.setTheme(theme) },
                colors =
                ButtonDefaults.buttonColors(
                  containerColor =
                  if (theme == currentTheme) {
                    MaterialTheme.colorScheme.primary
                  } else {
                    MaterialTheme.colorScheme.secondary
                  },
                ),
                modifier = Modifier.weight(1f),
              ) {
                Text(
                  text = theme.name,
                  style = MaterialTheme.typography.bodyMedium,
                  maxLines = 1,
                )
              }
            }
          }

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
                  GithubGraph(color = Color.Green)
                }
              }
            }
          }
        }
      }
    }
  }
}
