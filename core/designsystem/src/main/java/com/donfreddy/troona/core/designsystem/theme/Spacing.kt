/*
 * Copyright 2024 Don Freddy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.donfreddy.troona.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DefaultSpace = 0.dp
private val ExtraSmallSpace = 4.dp
private val SmallSpace = 8.dp
private val SmallMediumSpace = 12.dp
private val MediumSpace = 16.dp
private val ExtraMediumSpace = 24.dp
private val LargeSpace = 32.dp
private val ExtraLargeSpace = 40.dp
private val LargestSpace = 64.dp

@Immutable
data class Spacing(
  /**
   * 0dp
   */
  val default: Dp = DefaultSpace,

  /**
   * 4dp
   */
  val extraSmall: Dp = ExtraSmallSpace,

  /**
   * 8dp
   */
  val small: Dp = SmallSpace,

  /**
   * 12dp
   */
  val smallMedium: Dp = SmallMediumSpace,

  /**
   * 16dp
   */
  val medium: Dp = MediumSpace,

  /**
   * 24dp
   */
  val extraMedium: Dp = ExtraMediumSpace,

  /**
   * 32dp
   */
  val large: Dp = LargeSpace,

  /**
   * 40dp
   */
  val extraLarge: Dp = ExtraLargeSpace,

  /**
   * 64dp
   */
  val largest: Dp = LargestSpace
)

internal val LocalSpacing = staticCompositionLocalOf { Spacing() }