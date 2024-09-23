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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.baselineprofile.app.bars.GroupedBarChartExample
import io.androidpoet.drafter.baselineprofile.app.bars.HistogramChartExample
import io.androidpoet.drafter.baselineprofile.app.bars.SimpleBarChartExample
import io.androidpoet.drafter.baselineprofile.app.bars.StackedBarChartExample
import io.androidpoet.drafter.baselineprofile.app.bars.WaterfallChartExample
import io.androidpoet.drafter.baselineprofile.app.line.GroupedLineChartExample
import io.androidpoet.drafter.baselineprofile.app.line.SimpleLineChartExample
import io.androidpoet.drafter.baselineprofile.app.line.StackedLineChartExample
import io.androidpoet.drafter.baselineprofile.app.pie.PieChartExample

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
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
        item { SimpleLineChartExample() }
        item { WaterfallChartExample() }
      }
    }
  }
}