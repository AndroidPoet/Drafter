package io.androidpoet.drafter.gauge.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.theme.DrafterColors

@Immutable
public data class GaugeData(
  val value: Float,
  val min: Float = 0f,
  val max: Float = 100f,
  val label: String = "",
  val color: Color = DrafterColors.Teal,
)
