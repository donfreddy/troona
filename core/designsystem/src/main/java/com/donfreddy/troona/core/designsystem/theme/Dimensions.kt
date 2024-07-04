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

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
  val grid25: Dp,
  val grid50: Dp,
  val grid100: Dp,
  val grid150: Dp,
  val grid200: Dp,
  val grid250: Dp,
  val grid300: Dp,
  val grid350: Dp,
  val grid400: Dp,
  val grid450: Dp,
  val grid500: Dp,
  val grid550: Dp,
  val grid600: Dp,
  val minimumTouchTarget: Dp = 48.dp,
)

val smallDimensions = Dimensions(
  grid25 = 1.5f.dp,
  grid50 = 3.dp,
  grid100 = 6.dp,
  grid150 = 9.dp,
  grid200 = 12.dp,
  grid250 = 15.dp,
  grid300 = 18.dp,
  grid350 = 21.dp,
  grid400 = 24.dp,
  grid450 = 27.dp,
  grid500 = 30.dp,
  grid550 = 33.dp,
  grid600 = 36.dp,
)

val sw360Dimensions = Dimensions(
  grid25 = 2.dp,
  grid50 = 4.dp,
  grid100 = 8.dp,
  grid150 = 12.dp,
  grid200 = 16.dp,
  grid250 = 20.dp,
  grid300 = 24.dp,
  grid350 = 28.dp,
  grid400 = 32.dp,
  grid450 = 36.dp,
  grid500 = 40.dp,
  grid550 = 44.dp,
  grid600 = 48.dp,
)

// Following the same pattern, we can define dimensions or integers
// for sw600dp (7 inch Tablets) and sw720dp (10 inch Tablets) whenever
// they make a comeback on Android.
// See: https://proandroiddev.com/supporting-different-screen-sizes-on-android-with-jetpack-compose-f215c13081bd

internal val LocalDimens = staticCompositionLocalOf { smallDimensions }