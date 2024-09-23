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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.androidpoet.drafterdemo.bars.GroupedBarChartExample
import io.androidpoet.drafterdemo.bars.HistogramChartExample
import io.androidpoet.drafterdemo.bars.SimpleBarChartExample
import io.androidpoet.drafterdemo.bars.StackedBarChartExample
import io.androidpoet.drafterdemo.bars.WaterfallChartExample
import io.androidpoet.drafterdemo.buble.BubbleChartExample
import io.androidpoet.drafterdemo.gantt.GanttChartExample
import io.androidpoet.drafterdemo.line.GroupedLineChartExample
import io.androidpoet.drafterdemo.line.ScatterPlotChartExample
import io.androidpoet.drafterdemo.line.SimpleLineChartExample
import io.androidpoet.drafterdemo.line.StackedLineChartExample
import io.androidpoet.drafterdemo.pie.DonutCharExample
import io.androidpoet.drafterdemo.pie.PieChartExample
import io.androidpoet.drafterdemo.radar.RadarChartExample
import io.androidpoet.drafterdemo.ui.theme.DrafterDemoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      DrafterDemoTheme {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(vertical = 16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          item { SimpleBarChartExample() }
          item { GroupedBarChartExample() }
          item { StackedBarChartExample() }
          item { SimpleLineChartExample() }
          item { GroupedLineChartExample() }
          item { StackedLineChartExample() }
          item { HistogramChartExample() }
          item { PieChartExample() }
          item { DonutCharExample() }
          item { ScatterPlotChartExample() }
          item { WaterfallChartExample() }
          item { RadarChartExample() }
          item { GanttChartExample() }
          item { BubbleChartExample() }
        }
      }
    }
  }
}
