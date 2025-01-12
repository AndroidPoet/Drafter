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
package io.androidpoet.drafterdemo.manager

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object ChartThemeManager {
  enum class ColorTheme {
    MATERIAL,
    MODERN,
    NATURE,
    PROFESSIONAL,
  }

  private val palettes =
    mapOf(
      ColorTheme.MATERIAL to
        listOf(
          Color(0xFF2196F3), // Primary Blue
          Color(0xFFFF9800), // Orange
          Color(0xFF4CAF50), // Green
          Color(0xFFF44336), // Red
          Color(0xFF9C27B0), // Purple
          Color(0xFF795548), // Brown
          Color(0xFF009688), // Teal
          Color(0xFFE91E63), // Pink
        ),
      ColorTheme.MODERN to
        listOf(
          Color(0xFF7E57C2), // Purple
          Color(0xFF26A69A), // Teal
          Color(0xFFFF7043), // Coral
          Color(0xFF66BB6A), // Light Green
          Color(0xFF5C6BC0), // Indigo
          Color(0xFFFFCA28), // Amber
          Color(0xFF42A5F5), // Light Blue
          Color(0xFFEC407A), // Pink
        ),
      ColorTheme.NATURE to
        listOf(
          Color(0xFF66BB6A), // Green
          Color(0xFF42A5F5), // Sky Blue
          Color(0xFFFFB74D), // Light Orange
          Color(0xFF8D6E63), // Brown
          Color(0xFF26A69A), // Sea Green
          Color(0xFFFFCC80), // Pale Orange
          Color(0xFF81C784), // Light Green
          Color(0xFF7986CB), // Blue Grey
        ),
      ColorTheme.PROFESSIONAL to
        listOf(
          Color(0xFF5C6BC0), // Indigo
          Color(0xFF8D6E63), // Brown
          Color(0xFF26A69A), // Teal
          Color(0xFF78909C), // Blue Grey
          Color(0xFF7E57C2), // Deep Purple
          Color(0xFF66BB6A), // Green
          Color(0xFF29B6F6), // Light Blue
          Color(0xFFBDBDBD), // Grey
        ),
    )

  private val githubColors =
    listOf(
      Color(0xFF0E4429), // Darkest
      Color(0xFF006D32), // Dark
      Color(0xFF26A641), // Medium
      Color(0xFF39D353), // Light
    )

  private fun getFullPalette(theme: ColorTheme): List<Color> =
    palettes[theme] ?: palettes[ColorTheme.MATERIAL]!!

  private val _currentTheme = mutableStateOf(ColorTheme.MATERIAL)
  private val _currentFullPalette = mutableStateOf(getFullPalette(ColorTheme.MATERIAL))
  val currentTheme: State<ColorTheme> = _currentTheme
  val palette: State<List<Color>> = _currentFullPalette

  fun setTheme(theme: ColorTheme) {
    _currentTheme.value = theme
    _currentFullPalette.value = getFullPalette(theme)
  }

  fun getGithubColors() = githubColors
}
