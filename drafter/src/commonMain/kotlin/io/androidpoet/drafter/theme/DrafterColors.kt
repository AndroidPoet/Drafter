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

import androidx.compose.ui.graphics.Color

/**
 * A curated, soft, premium colour palette for Drafter charts.
 *
 * These tones are deliberately desaturated and gentle — no harsh primary red
 * or blue — so charts read as calm and modern out of the box. Pass
 * [DrafterColors.palette] (or individual colours) into any chart's data model.
 */
public object DrafterColors {
  /** Calm sky blue — the default primary series colour. */
  public val Blue: Color = Color(0xFF4C8DF6)

  /** Soft aqua / teal — great as a secondary series. */
  public val Teal: Color = Color(0xFF2FC4C0)

  /** Gentle violet. */
  public val Violet: Color = Color(0xFF7C6BF2)

  /** Warm amber. */
  public val Amber: Color = Color(0xFFF6B24C)

  /** Fresh green. */
  public val Green: Color = Color(0xFF49C17A)

  /** Muted coral — a softened stand-in for red. */
  public val Coral: Color = Color(0xFFF2766B)

  /** Rose pink. */
  public val Pink: Color = Color(0xFFEC6B9A)

  /** Deep indigo. */
  public val Indigo: Color = Color(0xFF5B6BF0)

  /** Faint grid-line colour for light surfaces. */
  public val GridLight: Color = Color(0xFFEDF0F5)

  /** Faint grid-line colour for dark surfaces. */
  public val GridDark: Color = Color(0xFF2A2E37)

  /** Muted axis-label colour for light surfaces. */
  public val LabelLight: Color = Color(0xFF9AA3B2)

  /** Muted axis-label colour for dark surfaces. */
  public val LabelDark: Color = Color(0xFF8A92A2)

  /** Card/surface colour for light mode — used for slice separators. */
  public val SurfaceLight: Color = Color.White

  /** Card/surface colour for dark mode — used for slice separators. */
  public val SurfaceDark: Color = Color(0xFF1B1E25)

  /**
   * Ordered default palette. The first two entries (Blue, Teal) match the
   * "Point 01 / Point 02" pairing in modern dashboard designs.
   */
  public val palette: List<Color> =
    listOf(Blue, Teal, Violet, Amber, Green, Coral, Pink, Indigo)
}
