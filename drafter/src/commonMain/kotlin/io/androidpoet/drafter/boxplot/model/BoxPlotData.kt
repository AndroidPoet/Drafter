package io.androidpoet.drafter.boxplot.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.theme.DrafterColors

@Immutable
public data class BoxGroup(
  val label: String,
  val min: Float,
  val q1: Float,
  val median: Float,
  val q3: Float,
  val max: Float,
  val color: Color = DrafterColors.Violet,
)

@Immutable
public data class BoxPlotData(
  val groups: List<BoxGroup>,
)
