package io.androidpoet.drafter.sankey.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color

/**
 * A single node in a Sankey diagram.
 *
 * @param id stable identifier referenced by [SankeyLink.from] / [SankeyLink.to]
 * @param label human-readable text drawn beside the node bar
 * @param column the layer (0, 1, 2, ...) the node belongs to; columns are spread
 *   evenly across the chart width from left to right
 * @param color the colour of the node bar and the tint of its outgoing bands
 */
@Immutable
public data class SankeyNode(
  val id: String,
  val label: String,
  val column: Int,
  val color: Color,
)

/**
 * A flow between two nodes. Its [value] determines the thickness of the band at
 * both endpoints.
 *
 * @param from id of the source node (must sit in a lower column than [to])
 * @param to id of the destination node
 * @param value the magnitude of the flow; band thickness is proportional to it
 */
@Immutable
public data class SankeyLink(
  val from: String,
  val to: String,
  val value: Float,
)

/** The full dataset for a Sankey diagram: a set of [nodes] and the [links] between them. */
@Immutable
public data class SankeyData(
  val nodes: List<SankeyNode>,
  val links: List<SankeyLink>,
)
