package io.androidpoet.drafter.treemap.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color

@Immutable
public data class TreemapItem(
  val label: String,
  val value: Float,
  val color: Color,
)

@Immutable
public data class TreemapData(
  val items: List<TreemapItem>,
)
