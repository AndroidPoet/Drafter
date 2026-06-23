package io.androidpoet.drafter.sunburst.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color

@Immutable
public data class SunburstNode(
  val label: String,
  val value: Float,
  val color: Color,
  val children: List<SunburstNode> = emptyList(),
)

@Immutable
public data class SunburstData(
  val roots: List<SunburstNode>,
)
