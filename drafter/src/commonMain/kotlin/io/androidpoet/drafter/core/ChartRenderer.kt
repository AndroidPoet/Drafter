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
package io.androidpoet.drafter.core

/**
 * Common supertype implemented by every chart renderer in Drafter.
 *
 * Each chart family still exposes its own renderer with a draw signature tailored to
 * its data, but they all share this contract so callers can hold, group, and reason
 * about renderers uniformly (e.g. `List<ChartRenderer>`), and so future cross-cutting
 * capabilities can be added in one place.
 */
public interface ChartRenderer
