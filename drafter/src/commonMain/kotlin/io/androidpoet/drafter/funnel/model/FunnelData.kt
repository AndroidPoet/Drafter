package io.androidpoet.drafter.funnel.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color

@Immutable
public data class FunnelStage(
  val label: String,
  val value: Float,
  val color: Color,
)

@Immutable
public data class FunnelData(
  val stages: List<FunnelStage>,
)
