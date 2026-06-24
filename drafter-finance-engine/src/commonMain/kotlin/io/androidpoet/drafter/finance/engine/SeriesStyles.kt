/*
 * Designed and developed by 2024 androidpoet (Ranbir Singh)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.androidpoet.drafter.finance.engine

import io.androidpoet.drafter.finance.engine.scene.ChartColor

/**
 * Per-series customization options, modelled on TradingView Lightweight Charts'
 * series option sets — each series exposes the same knobs (colors, widths, base
 * values) so callers get the same level of control.
 */
public data class LineSeriesStyle(
  public val color: ChartColor,
  public val lineWidth: Float = 2f,
)

public data class AreaSeriesStyle(
  public val lineColor: ChartColor,
  public val fillColor: ChartColor,
  public val lineWidth: Float = 2f,
)

public data class BaselineSeriesStyle(
  public val baseValue: Float,
  public val topLineColor: ChartColor,
  public val topFillColor: ChartColor,
  public val bottomLineColor: ChartColor,
  public val bottomFillColor: ChartColor,
  public val lineWidth: Float = 2f,
)

public data class HistogramSeriesStyle(
  public val color: ChartColor,
  public val baseValue: Float = 0f,
  public val barWidthRatio: Float = 0.7f,
)

public data class BarSeriesStyle(
  public val up: ChartColor,
  public val down: ChartColor,
  public val thickness: Float = 1.5f,
  public val tickRatio: Float = 0.3f,
)

public data class VolumeStyle(
  public val up: ChartColor,
  public val down: ChartColor,
  public val barWidthRatio: Float = 0.7f,
)
