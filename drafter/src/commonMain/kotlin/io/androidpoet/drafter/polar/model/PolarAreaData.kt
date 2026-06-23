package io.androidpoet.drafter.polar.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color

@Immutable
public data class PolarSlice(
  val label: String,
  val value: Float,
  val color: Color,
)

@Immutable
public data class PolarAreaData(
  val slices: List<PolarSlice>,
)
