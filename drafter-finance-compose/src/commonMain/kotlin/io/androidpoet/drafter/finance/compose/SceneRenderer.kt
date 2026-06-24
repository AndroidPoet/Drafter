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
package io.androidpoet.drafter.finance.compose

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.finance.engine.scene.ChartColor
import io.androidpoet.drafter.finance.engine.scene.DrawCommand
import io.androidpoet.drafter.finance.engine.scene.FillPathCmd
import io.androidpoet.drafter.finance.engine.scene.LineCmd
import io.androidpoet.drafter.finance.engine.scene.PolylineCmd
import io.androidpoet.drafter.finance.engine.scene.RectCmd
import io.androidpoet.drafter.finance.engine.scene.Scene
import io.androidpoet.drafter.finance.engine.scene.TextAlign
import io.androidpoet.drafter.finance.engine.scene.TextCmd

/** Converts an engine [ChartColor] (packed ARGB) into a Compose [Color]. */
internal fun ChartColor.toComposeColor(): Color =
  Color(red = red / 255f, green = green / 255f, blue = blue / 255f, alpha = alpha / 255f)

/**
 * Walks a [Scene]'s display list and draws each primitive with native Compose
 * APIs. This is the entire Compose renderer — it holds NO chart logic; every
 * coordinate was already computed by the engine. The SwiftUI renderer is the
 * exact same walk in `Canvas`.
 */
public fun DrawScope.drawScene(scene: Scene, textMeasurer: TextMeasurer) {
  for (command in scene.commands) {
    drawCommand(command, textMeasurer)
  }
}

private fun DrawScope.drawCommand(command: DrawCommand, textMeasurer: TextMeasurer) {
  when (command) {
    is LineCmd ->
      drawLine(
        color = command.color.toComposeColor(),
        start = Offset(command.x1, command.y1),
        end = Offset(command.x2, command.y2),
        strokeWidth = command.strokeWidth,
        cap = StrokeCap.Round,
      )

    is RectCmd -> {
      val color = command.color.toComposeColor()
      val topLeft = Offset(command.rect.left, command.rect.top)
      val size = Size(command.rect.width, command.rect.height)
      val style = if (command.fill) Fill else Stroke(width = command.strokeWidth)
      if (command.cornerRadius > 0f) {
        drawRoundRect(
          color = color,
          topLeft = topLeft,
          size = size,
          cornerRadius = CornerRadius(command.cornerRadius, command.cornerRadius),
          style = style,
        )
      } else {
        drawRect(color = color, topLeft = topLeft, size = size, style = style)
      }
    }

    is PolylineCmd -> {
      if (command.points.size >= 2) {
        val path = Path()
        val first = command.points.first()
        path.moveTo(first.x, first.y)
        for (i in 1 until command.points.size) {
          path.lineTo(command.points[i].x, command.points[i].y)
        }
        drawPath(
          path = path,
          color = command.color.toComposeColor(),
          style = Stroke(
            width = command.strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
          ),
        )
      }
    }

    is FillPathCmd -> {
      if (command.points.size >= 2) {
        val path = Path()
        val first = command.points.first()
        path.moveTo(first.x, first.y)
        for (i in 1 until command.points.size) {
          path.lineTo(command.points[i].x, command.points[i].y)
        }
        path.close()
        drawPath(path = path, color = command.color.toComposeColor(), style = Fill)
      }
    }

    is TextCmd -> {
      val style = TextStyle(color = command.color.toComposeColor(), fontSize = command.sizeSp.sp)
      val measured = textMeasurer.measure(command.text, style)
      val dx = when (command.align) {
        TextAlign.Start -> 0f
        TextAlign.Center -> -measured.size.width / 2f
        TextAlign.End -> -measured.size.width.toFloat()
      }
      drawText(
        textMeasurer = textMeasurer,
        text = command.text,
        topLeft = Offset(command.x + dx, command.y),
        style = style,
      )
    }
  }
}
