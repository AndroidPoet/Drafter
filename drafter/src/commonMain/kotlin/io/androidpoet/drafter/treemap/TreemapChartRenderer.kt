package io.androidpoet.drafter.treemap

import androidx.compose.runtime.Immutable

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp
import io.androidpoet.drafter.treemap.model.TreemapData
import io.androidpoet.drafter.treemap.model.TreemapItem

/** A laid-out tile: the source item plus the pixel rectangle it occupies. */
internal data class TreemapTile(
  val item: TreemapItem,
  val rect: Rect,
)

public interface TreemapRenderer : io.androidpoet.drafter.core.ChartRenderer {
  public fun draw(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  )
}

@Immutable
public class TreemapChartRenderer(
  public val data: TreemapData,
) : TreemapRenderer {
  override fun draw(
    drawScope: DrawScope,
    chartLeft: Float,
    chartTop: Float,
    chartWidth: Float,
    chartHeight: Float,
    animationProgress: Float,
    isSystemInDarkTheme: Boolean,
    textMeasurer: TextMeasurer,
  ) {
    if (chartWidth <= 0f || chartHeight <= 0f) return

    val sorted = data.items
      .filter { it.value > 0f }
      .sortedByDescending { it.value }
    if (sorted.isEmpty()) return

    val bounds = Rect(chartLeft, chartTop, chartLeft + chartWidth, chartTop + chartHeight)
    val tiles = layout(sorted, bounds)

    tiles.forEachIndexed { index, tile ->
      drawScope.drawTile(
        tile = tile,
        index = index,
        count = tiles.size,
        animationProgress = animationProgress,
        isSystemInDarkTheme = isSystemInDarkTheme,
        textMeasurer = textMeasurer,
      )
    }
  }

  /**
   * Slice-and-dice layout. Recursively splits the remaining rectangle along its
   * longer side, peeling off a "row" of the largest items so each tile's aspect
   * ratio stays close to 1.
   */
  internal fun layout(items: List<TreemapItem>, bounds: Rect): List<TreemapTile> {
    val tiles = ArrayList<TreemapTile>(items.size)
    squarify(items, bounds, tiles)
    return tiles
  }

  private fun squarify(items: List<TreemapItem>, rect: Rect, out: MutableList<TreemapTile>) {
    if (items.isEmpty() || rect.width <= 0f || rect.height <= 0f) return
    if (items.size == 1) {
      out.add(TreemapTile(items.first(), rect))
      return
    }

    val total = items.sumOf { it.value.toDouble() }.toFloat()
    if (total <= 0f) return

    // Lay tiles along the shorter side so rows stay close to square.
    val horizontal = rect.width >= rect.height
    val sideLength = if (horizontal) rect.height else rect.width

    // Greedily grow a row, stopping when adding the next item worsens aspect ratio.
    var rowEnd = 1
    var rowSum = items[0].value
    var bestRatio = worstAspectRatio(items.subList(0, 1), sideLength, rowSum, rect, total)
    while (rowEnd < items.size) {
      val candidate = items.subList(0, rowEnd + 1)
      val candidateSum = rowSum + items[rowEnd].value
      val candidateRatio = worstAspectRatio(candidate, sideLength, candidateSum, rect, total)
      if (candidateRatio > bestRatio) break
      bestRatio = candidateRatio
      rowSum = candidateSum
      rowEnd++
    }

    val row = items.subList(0, rowEnd)
    val rest = items.subList(rowEnd, items.size)

    // Fraction of the whole rect's area consumed by this row.
    val rowAreaFraction = rowSum / total

    if (horizontal) {
      val rowWidth = rect.width * rowAreaFraction
      val rowRect = Rect(rect.left, rect.top, rect.left + rowWidth, rect.bottom)
      placeRow(row, rowRect, horizontal = false)
        .forEach { out.add(it) }
      squarify(rest, Rect(rect.left + rowWidth, rect.top, rect.right, rect.bottom), out)
    } else {
      val rowHeight = rect.height * rowAreaFraction
      val rowRect = Rect(rect.left, rect.top, rect.right, rect.top + rowHeight)
      placeRow(row, rowRect, horizontal = true)
        .forEach { out.add(it) }
      squarify(rest, Rect(rect.left, rect.top + rowHeight, rect.right, rect.bottom), out)
    }
  }

  /** Lay [row] items out evenly across [rowRect], stacking along its length. */
  private fun placeRow(
    row: List<TreemapItem>,
    rowRect: Rect,
    horizontal: Boolean,
  ): List<TreemapTile> {
    val rowTotal = row.sumOf { it.value.toDouble() }.toFloat()
    if (rowTotal <= 0f) return emptyList()
    val tiles = ArrayList<TreemapTile>(row.size)
    var cursor = if (horizontal) rowRect.left else rowRect.top
    row.forEach { item ->
      val frac = item.value / rowTotal
      if (horizontal) {
        val w = rowRect.width * frac
        tiles.add(TreemapTile(item, Rect(cursor, rowRect.top, cursor + w, rowRect.bottom)))
        cursor += w
      } else {
        val h = rowRect.height * frac
        tiles.add(TreemapTile(item, Rect(rowRect.left, cursor, rowRect.right, cursor + h)))
        cursor += h
      }
    }
    return tiles
  }

  /** Worst (max) aspect ratio among the row if it were placed, for the squarify heuristic. */
  private fun worstAspectRatio(
    row: List<TreemapItem>,
    sideLength: Float,
    rowSum: Float,
    rect: Rect,
    total: Float,
  ): Float {
    if (rowSum <= 0f || sideLength <= 0f) return Float.MAX_VALUE
    val rectArea = rect.width * rect.height
    val rowArea = rectArea * (rowSum / total)
    val rowThickness = rowArea / sideLength
    if (rowThickness <= 0f) return Float.MAX_VALUE
    var worst = 0f
    row.forEach { item ->
      val itemArea = rectArea * (item.value / total)
      val itemLength = itemArea / rowThickness
      if (itemLength > 0f) {
        val ratio = maxOf(rowThickness / itemLength, itemLength / rowThickness)
        if (ratio > worst) worst = ratio
      }
    }
    return worst
  }
}

private const val GAP = 4f
private const val CORNER = 8f

internal fun DrawScope.drawTile(
  tile: TreemapTile,
  index: Int,
  count: Int,
  animationProgress: Float,
  isSystemInDarkTheme: Boolean,
  textMeasurer: TextMeasurer,
) {
  val rect = tile.rect
  val innerLeft = rect.left + GAP
  val innerTop = rect.top + GAP
  val innerWidth = rect.width - GAP * 2f
  val innerHeight = rect.height - GAP * 2f
  if (innerWidth <= 1f || innerHeight <= 1f) return

  // Staggered fade + scale-from-center reveal.
  val stagger = if (count > 1) (index.toFloat() / count) * 0.4f else 0f
  val local = ((animationProgress - stagger) / (1f - stagger)).coerceIn(0f, 1f)
  if (local <= 0f) return
  val scale = 0.6f + 0.4f * local
  val alpha = local

  val drawW = innerWidth * scale
  val drawH = innerHeight * scale
  val centerX = innerLeft + innerWidth / 2f
  val centerY = innerTop + innerHeight / 2f
  val topLeft = Offset(centerX - drawW / 2f, centerY - drawH / 2f)
  val tileSize = Size(drawW, drawH)
  val corner = CornerRadius(CORNER, CORNER)

  val base = tile.item.color
  val gradient = Brush.verticalGradient(
    colors = listOf(
      base.copy(alpha = alpha),
      base.copy(alpha = alpha * 0.78f),
    ),
    startY = topLeft.y,
    endY = topLeft.y + drawH,
  )

  drawRoundRect(
    brush = gradient,
    topLeft = topLeft,
    size = tileSize,
    cornerRadius = corner,
  )
  // Subtle inner highlight stroke for a premium, glassy edge.
  drawRoundRect(
    color = Color.White.copy(alpha = 0.12f * alpha),
    topLeft = topLeft,
    size = tileSize,
    cornerRadius = corner,
    style = Stroke(width = 1f),
  )

  drawTileLabel(tile.item, topLeft, tileSize, alpha, textMeasurer)
}

private fun DrawScope.drawTileLabel(
  item: TreemapItem,
  topLeft: Offset,
  tileSize: Size,
  alpha: Float,
  textMeasurer: TextMeasurer,
) {
  // Skip text when the tile is too small to read.
  if (tileSize.width < 48f || tileSize.height < 32f) return

  val labelStyle = TextStyle(
    fontSize = 12.sp,
    color = Color.White.copy(alpha = alpha),
  )
  val valueStyle = TextStyle(
    fontSize = 10.sp,
    color = Color.White.copy(alpha = alpha * 0.85f),
  )

  val pad = 8f
  val maxTextWidth = (tileSize.width - pad * 2f).toInt()
  if (maxTextWidth <= 0) return

  val labelLayout = textMeasurer.measure(item.label, labelStyle)
  if (labelLayout.size.width > maxTextWidth && tileSize.width < labelLayout.size.width + pad) {
    // Even the label alone doesn't fit comfortably.
    if (tileSize.width < 56f) return
  }

  drawText(
    textMeasurer = textMeasurer,
    text = item.label,
    style = labelStyle,
    topLeft = Offset(topLeft.x + pad, topLeft.y + pad),
  )

  val valueText = formatValue(item.value)
  if (tileSize.height >= 48f) {
    drawText(
      textMeasurer = textMeasurer,
      text = valueText,
      style = valueStyle,
      topLeft = Offset(topLeft.x + pad, topLeft.y + pad + labelLayout.size.height + 2f),
    )
  }
}

private fun formatValue(value: Float): String {
  return if (value == value.toInt().toFloat()) {
    value.toInt().toString()
  } else {
    val scaled = kotlin.math.round(value * 10f) / 10f
    scaled.toString()
  }
}
