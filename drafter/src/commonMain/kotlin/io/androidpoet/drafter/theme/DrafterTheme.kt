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
package io.androidpoet.drafter.theme

import androidx.compose.runtime.Immutable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Resolved set of colors a chart should paint with. Charts read this from
 * [LocalDrafterTheme] instead of branching on a raw `isSystemInDarkTheme` boolean,
 * which is what lets a consumer supply a custom palette in one place and have every
 * chart pick it up.
 */
@Immutable
public data class DrafterThemeColors(
  val palette: List<Color>,
  val grid: Color,
  val label: Color,
  val surface: Color,
  val isDark: Boolean,
)

/** Default light color set, derived from [DrafterColors]. */
public fun lightDrafterColors(): DrafterThemeColors =
  DrafterThemeColors(
    palette = DrafterColors.palette,
    grid = DrafterColors.GridLight,
    label = DrafterColors.LabelLight,
    surface = DrafterColors.SurfaceLight,
    isDark = false,
  )

/** Default dark color set, derived from [DrafterColors]. */
public fun darkDrafterColors(): DrafterThemeColors =
  DrafterThemeColors(
    palette = DrafterColors.palette,
    grid = DrafterColors.GridDark,
    label = DrafterColors.LabelDark,
    surface = DrafterColors.SurfaceDark,
    isDark = true,
  )

/** Ambient theme for all Drafter charts. Defaults to the light color set. */
public val LocalDrafterTheme: ProvidableCompositionLocal<DrafterThemeColors> =
  staticCompositionLocalOf { lightDrafterColors() }

/**
 * Provides a [DrafterThemeColors] to every chart in [content]. Pass [colors] to fully
 * customize the palette; otherwise a default light/dark set is chosen from [dark].
 */
@Composable
public fun DrafterTheme(
  dark: Boolean = isSystemInDarkTheme(),
  colors: DrafterThemeColors = if (dark) darkDrafterColors() else lightDrafterColors(),
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(LocalDrafterTheme provides colors, content = content)
}
