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
package io.androidpoet.drafter.popup

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup

@Composable
public fun Tooltip(text: String) {
  Popup(
    alignment = Alignment.TopStart,
  ) {
    Surface(
      modifier =
      Modifier
        .shadow(4.dp)
        .padding(4.dp),
      color = Color.White,
      shape = RoundedCornerShape(4.dp),
    ) {
      Text(
        text = text,
        modifier = Modifier.padding(8.dp),
        fontSize = 12.sp,
      )
    }
  }
}

// Add this data class to store hover state information
public data class HoverState(
  val isHovered: Boolean = false,
  val position: Offset = Offset.Zero,
  val value: String = "",
)
