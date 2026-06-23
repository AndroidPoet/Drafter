package io.androidpoet.drafter.area.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.theme.DrafterColors

@Immutable
public data class AreaChartData(
  val labels: List<String>,
  val values: List<Float>,
  val color: Color = DrafterColors.Blue,
)
