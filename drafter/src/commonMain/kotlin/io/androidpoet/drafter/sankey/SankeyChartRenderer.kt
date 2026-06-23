package io.androidpoet.drafter.sankey

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.sankey.model.SankeyData
import io.androidpoet.drafter.sankey.model.SankeyNode
import io.androidpoet.drafter.theme.DrafterColors

/**
 * Renders a [SankeyData] flow diagram: layered node bars connected by smooth,
 * gradient-filled bands whose thickness encodes each link's value.
 *
 * Layout is pure Kotlin: nodes are grouped by [SankeyNode.column], each node's
 * height is proportional to its throughput (max of total inflow vs outflow), and
 * links are allocated stacked offsets along each node edge so they never overlap.
 */
@Immutable
public class SankeyChartRenderer(
  public val data: SankeyData,
) : io.androidpoet.drafter.core.ChartRenderer {
  /** A node positioned in pixel space, ready to draw. */
  private data class PlacedNode(
    val node: SankeyNode,
    val x: Float,
    val top: Float,
    val width: Float,
    val fullHeight: Float,
    val isFirst: Boolean,
    val isLast: Boolean,
  )

  public fun draw(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  ) {
    if (data.nodes.isEmpty()) return

    val progress = animationProgress.coerceIn(0f, 1f)
    val nodeById = data.nodes.associateBy { it.id }

    // Per-node throughput = max(total inflow, total outflow).
    val inflow = mutableMapOf<String, Float>()
    val outflow = mutableMapOf<String, Float>()
    for (link in data.links) {
      if (link.from in nodeById && link.to in nodeById) {
        outflow[link.from] = (outflow[link.from] ?: 0f) + link.value
        inflow[link.to] = (inflow[link.to] ?: 0f) + link.value
      }
    }
    val throughput: (String) -> Float = { id ->
      maxOf(inflow[id] ?: 0f, outflow[id] ?: 0f).coerceAtLeast(0f)
    }

    // Group by column and order columns left -> right (multiplatform: no java.util.SortedMap).
    val columns: Map<Int, List<SankeyNode>> =
      data.nodes.groupBy { it.column }.entries
        .sortedBy { it.key }
        .associate { it.key to it.value }
    val columnKeys = columns.keys.toList()
    val maxColumn = columnKeys.last()

    val nodeWidth = (chartWidth * 0.045f).coerceIn(6f, 26f)
    val verticalGap = (chartHeight * 0.04f).coerceAtLeast(6f)

    // Scale node heights so the tallest column's stack fits the chart height.
    val maxThroughputSum =
      columns.values.maxOf { group ->
        group.sumOf { throughput(it.id).toDouble() }.toFloat()
      }.coerceAtLeast(1f)
    val availableForBars =
      (chartHeight - ((columns.values.maxOf { it.size } - 1).coerceAtLeast(0) * verticalGap))
        .coerceAtLeast(1f)
    val valueToPx = availableForBars / maxThroughputSum

    // Place every node.
    val placed = mutableMapOf<String, PlacedNode>()
    columnKeys.forEachIndexed { colIndex, colKey ->
      val group = columns.getValue(colKey).sortedBy { it.label }
      val heights = group.map { (throughput(it.id) * valueToPx).coerceAtLeast(2f) }
      val stackHeight = heights.sum() + (group.size - 1).coerceAtLeast(0) * verticalGap
      val startY = chartTop + (chartHeight - stackHeight) / 2f

      val x =
        if (maxColumn == 0) {
          chartLeft + (chartWidth - nodeWidth) / 2f
        } else {
          chartLeft + (chartWidth - nodeWidth) * (colKey.toFloat() / maxColumn.toFloat())
        }

      var cursorY = startY
      group.forEachIndexed { i, node ->
        val h = heights[i]
        placed[node.id] =
          PlacedNode(
            node = node,
            x = x,
            top = cursorY,
            width = nodeWidth,
            fullHeight = h,
            isFirst = colIndex == 0,
            isLast = colIndex == columnKeys.lastIndex,
          )
        cursorY += h + verticalGap
      }
    }

    // Running offsets so multiple links share each edge without overlapping.
    val outOffset = mutableMapOf<String, Float>()
    val inOffset = mutableMapOf<String, Float>()

    // Draw the bands first (behind node bars).
    for (link in data.links) {
      val from = placed[link.from] ?: continue
      val to = placed[link.to] ?: continue

      val fromTotal = (throughput(from.node.id)).coerceAtLeast(0.0001f)
      val toTotal = (throughput(to.node.id)).coerceAtLeast(0.0001f)
      val fromThickness = from.fullHeight * (link.value / fromTotal)
      val toThickness = to.fullHeight * (link.value / toTotal)

      val oStart = outOffset[link.from] ?: 0f
      val iStart = inOffset[link.to] ?: 0f
      outOffset[link.from] = oStart + fromThickness
      inOffset[link.to] = iStart + toThickness

      val startX = from.x + from.width
      val endX = to.x

      val startTop = from.top + oStart
      val startBottom = startTop + fromThickness
      val endTop = to.top + iStart
      val endBottom = endTop + toThickness

      drawScope.drawBand(
        startX = startX,
        endX = endX,
        startTop = startTop,
        startBottom = startBottom,
        endTop = endTop,
        endBottom = endBottom,
        fromColor = from.node.color,
        toColor = to.node.color,
        revealRight = chartLeft + chartWidth * progress,
      )
    }

    // Draw node bars (animated growth in height) + labels.
    val labelColor = if (isSystemInDarkTheme) DrafterColors.LabelDark else DrafterColors.LabelLight
    val labelStyle = TextStyle(fontSize = 11.sp, color = labelColor)

    for (pn in placed.values) {
      val animHeight = pn.fullHeight * progress
      val centerY = pn.top + pn.fullHeight / 2f
      val barTop = centerY - animHeight / 2f
      val corner = CornerRadius((pn.width / 2.5f), (pn.width / 2.5f))

      drawScope.drawRoundRect(
        color = pn.node.color,
        topLeft = Offset(pn.x, barTop),
        size = Size(pn.width, animHeight),
        cornerRadius = corner,
      )
      // Soft white inner stroke for a crisp, premium edge.
      drawScope.drawRoundRect(
        color = Color.White.copy(alpha = 0.25f),
        topLeft = Offset(pn.x, barTop),
        size = Size(pn.width, animHeight),
        cornerRadius = corner,
        style = Stroke(width = 1.25f),
      )

      drawScope.drawNodeLabel(
        textMeasurer = textMeasurer,
        node = pn.node,
        style = labelStyle,
        x = pn.x,
        width = pn.width,
        centerY = centerY,
        barTop = barTop,
        isFirst = pn.isFirst,
        isLast = pn.isLast,
        progress = progress,
      )
    }
  }

  /** Draws one flow band as a filled S-curve with a horizontal from→to gradient. */
  private fun DrawScope.drawBand(
    startX: Float,
    endX: Float,
    startTop: Float,
    startBottom: Float,
    endTop: Float,
    endBottom: Float,
    fromColor: Color,
    toColor: Color,
    revealRight: Float,
  ) {
    val midX = (startX + endX) / 2f
    val path =
      Path().apply {
        moveTo(startX, startTop)
        cubicTo(midX, startTop, midX, endTop, endX, endTop)
        lineTo(endX, endBottom)
        cubicTo(midX, endBottom, midX, startBottom, startX, startBottom)
        close()
      }
    val brush =
      Brush.linearGradient(
        colors = listOf(fromColor.copy(alpha = 0.5f), toColor.copy(alpha = 0.5f)),
        start = Offset(startX, 0f),
        end = Offset(endX, 0f),
      )
    clipRect(right = revealRight) {
      drawPath(path = path, brush = brush, style = Fill)
    }
  }

  /** Draws a node's label: left of first column, right of last column, above otherwise. */
  private fun DrawScope.drawNodeLabel(
    textMeasurer: TextMeasurer,
    node: SankeyNode,
    style: TextStyle,
    x: Float,
    width: Float,
    centerY: Float,
    barTop: Float,
    isFirst: Boolean,
    isLast: Boolean,
    progress: Float,
  ) {
    if (node.label.isEmpty()) return
    val faded = style.copy(color = style.color.copy(alpha = style.color.alpha * progress))
    val layout = textMeasurer.measure(node.label, faded)
    val pad = 8f
    val topLeft =
      when {
        isFirst ->
          Offset(x - layout.size.width - pad, centerY - layout.size.height / 2f)
        isLast ->
          Offset(x + width + pad, centerY - layout.size.height / 2f)
        else ->
          Offset(x + width / 2f - layout.size.width / 2f, barTop - layout.size.height - 4f)
      }
    drawText(textMeasurer = textMeasurer, text = node.label, style = faded, topLeft = topLeft)
  }
}
