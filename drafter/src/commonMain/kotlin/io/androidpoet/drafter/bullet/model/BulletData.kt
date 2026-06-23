package io.androidpoet.drafter.bullet.model

import androidx.compose.runtime.Immutable

import androidx.compose.ui.graphics.Color
import io.androidpoet.drafter.theme.DrafterColors

@Immutable
public data class BulletMetric(
  val label: String,
  val value: Float,
  val target: Float,
  val ranges: List<Float>,
  val color: Color = DrafterColors.Indigo,
)

@Immutable
public data class BulletData(
  val metrics: List<BulletMetric>,
)
