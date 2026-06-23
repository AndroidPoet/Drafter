package io.androidpoet.drafter.stepline.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.theme.DrafterColors

@Immutable
public data class StepLineChartData(
  val labels: List<String>,
  val values: List<Float>,
  val color: Color = DrafterColors.Teal,
)
