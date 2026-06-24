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
package io.androidpoet.drafter.finance.engine.scene

import io.androidpoet.drafter.finance.engine.geometry.FPoint
import io.androidpoet.drafter.finance.engine.geometry.FRect

/** How a [TextCmd] is anchored horizontally around its origin x. */
public enum class TextAlign { Start, Center, End }

/**
 * A single drawing primitive in pixel space. The engine emits a list of these;
 * each platform renderer walks the list and draws it with native APIs. This is
 * the cross-language contract — keep it small and dumb.
 */
public sealed interface DrawCommand

public data class LineCmd(
  public val x1: Float,
  public val y1: Float,
  public val x2: Float,
  public val y2: Float,
  public val color: ChartColor,
  public val strokeWidth: Float,
) : DrawCommand

public data class RectCmd(
  public val rect: FRect,
  public val color: ChartColor,
  public val fill: Boolean,
  public val strokeWidth: Float = 0f,
  public val cornerRadius: Float = 0f,
) : DrawCommand

public data class PolylineCmd(
  public val points: List<FPoint>,
  public val color: ChartColor,
  public val strokeWidth: Float,
) : DrawCommand

/** A filled polygon (the [points] are closed automatically). Used for area fills. */
public data class FillPathCmd(
  public val points: List<FPoint>,
  public val color: ChartColor,
) : DrawCommand

public data class TextCmd(
  public val text: String,
  public val x: Float,
  public val y: Float,
  public val color: ChartColor,
  public val sizeSp: Float,
  public val align: TextAlign = TextAlign.Start,
) : DrawCommand

/** The full output of an engine build: an ordered display list plus its plot rect. */
public data class Scene(
  public val commands: List<DrawCommand>,
  public val plot: FRect,
)
