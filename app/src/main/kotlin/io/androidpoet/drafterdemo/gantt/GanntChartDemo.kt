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
package io.androidpoet.drafterdemo.gantt

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.androidpoet.drafter.gant.GanttChart
import io.androidpoet.drafter.gant.GanttChartData
import io.androidpoet.drafter.gant.GanttTask
import io.androidpoet.drafter.gant.SimpleGanttChartRenderer
import io.androidpoet.drafterdemo.ChartTitle

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
