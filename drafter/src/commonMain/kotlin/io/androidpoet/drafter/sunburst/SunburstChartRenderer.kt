package io.androidpoet.drafter.sunburst

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.sunburst.model.SunburstData
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

public interface SunburstRenderer : io.androidpoet.drafter.core.ChartRenderer {
  public fun draw(
    drawScope: DrawScope,
    centerX: Float,
    centerY: Float,
    maxRadius: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  )
}

@Immutable
public class SunburstChartRenderer(
  public val data: SunburstData,
) : SunburstRenderer {
  override fun draw(
    drawScope: DrawScope,
    centerX: Float,
    centerY: Float,
    maxRadius: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  ) {
    val roots = data.roots
    if (roots.isEmpty() || maxRadius <= 0f) return

    val center = Offset(centerX, centerY)
    val total = max(roots.sumOf { it.value.toDouble() }.toFloat(), 0.0001f)

    // Geometry: small center hole, inner ring (roots), outer ring (children).
    val holeRadius = maxRadius * 0.22f
    val innerOuter = maxRadius * 0.60f
    val outerOuter = maxRadius

    var cursor = -90f
    roots.forEach { root ->
      val rootSweep = (root.value / total) * 360f * animationProgress
      val rootStart = cursor

      // Inner ring wedge.
      drawScope.drawRingWedge(
        center = center,
        innerRadius = holeRadius,
        outerRadius = innerOuter,
        startAngle = rootStart,
        sweepAngle = rootSweep,
        color = root.color,
      )
      drawScope.drawRingLabel(
        center = center,
        radius = (holeRadius + innerOuter) / 2f,
        startAngle = rootStart,
        sweepAngle = rootSweep,
        label = root.label,
        isSystemInDarkTheme = isSystemInDarkTheme,
        textMeasurer = textMeasurer,
      )

      // Outer ring: children subdivide the parent's full angular span.
      val childTotal = max(root.children.sumOf { it.value.toDouble() }.toFloat(), 0.0001f)
      val fullRootSweep = (root.value / total) * 360f
      var childCursor = rootStart
      root.children.forEach { child ->
        val childSweep = (child.value / childTotal) * fullRootSweep * animationProgress
        val shade = lerp(child.color, Color.White, 0.30f)
        drawScope.drawRingWedge(
          center = center,
          innerRadius = innerOuter,
          outerRadius = outerOuter,
          startAngle = childCursor,
          sweepAngle = childSweep,
          color = shade,
        )
        drawScope.drawRingLabel(
          center = center,
          radius = (innerOuter + outerOuter) / 2f,
          startAngle = childCursor,
          sweepAngle = childSweep,
          label = child.label,
          isSystemInDarkTheme = isSystemInDarkTheme,
          textMeasurer = textMeasurer,
        )
        childCursor += childSweep
      }

      cursor += rootSweep
    }
  }
}

internal fun DrawScope.drawRingWedge(
  center: Offset,
  innerRadius: Float,
  outerRadius: Float,
  startAngle: Float,
  sweepAngle: Float,
  color: Color,
) {
  if (sweepAngle <= 0f) return
  // Draw the annular wedge as a thick stroked arc along the ring mid-line.
  val midRadius = (innerRadius + outerRadius) / 2f
  val thickness = outerRadius - innerRadius
  val topLeft = Offset(center.x - midRadius, center.y - midRadius)
  val arcSize = Size(midRadius * 2f, midRadius * 2f)

  drawArc(
    color = color,
    startAngle = startAngle,
    sweepAngle = sweepAngle,
    useCenter = false,
    topLeft = topLeft,
    size = arcSize,
    style = Stroke(width = thickness),
  )
  // Soft white separator stroke at the wedge's leading edge for crisp segmentation.
  drawArc(
    color = Color.White.copy(alpha = 0.5f),
    startAngle = startAngle,
    sweepAngle = sweepAngle,
    useCenter = false,
    topLeft = topLeft,
    size = arcSize,
    style = Stroke(width = 1f),
  )
}

internal fun DrawScope.drawRingLabel(
  center: Offset,
  radius: Float,
  startAngle: Float,
  sweepAngle: Float,
  label: String,
  isSystemInDarkTheme: Boolean,
  textMeasurer: TextMeasurer,
) {
  // Only label segments wide enough to fit text.
  if (sweepAngle < 18f) return
  val style =
    TextStyle(
      fontSize = 9.sp,
      color = if (isSystemInDarkTheme) Color.White else Color.Black,
    )
  val midDeg = startAngle + sweepAngle / 2f
  val midRad = (midDeg * (kotlin.math.PI / 180f)).toFloat()
  val lx = center.x + cos(midRad) * radius
  val ly = center.y + sin(midRad) * radius
  val layout = textMeasurer.measure(label, style)
  drawText(
    textMeasurer = textMeasurer,
    text = label,
    style = style,
    topLeft = Offset(lx - layout.size.width / 2f, ly - layout.size.height / 2f),
  )
}
