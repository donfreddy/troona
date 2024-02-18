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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object TroonaColor {
  val Purple80 = Color(0xFFD0BCFF)
  val PurpleGrey80 = Color(0xFFCCC2DC)
  val Pink80 = Color(0xFFEFB8C8)

  val Purple40 = Color(0xFF6650a4)
  val PurpleGrey40 = Color(0xFF625b71)
  val Pink40 = Color(0xFF7D5260)


  val PrimaryColor = Color(0xFFDD3F5D)
  //val PrimaryColor = Color(0xFFFF3B30)

  // Suggested
  val White = Color(0xFFFFFFFF)
  val Grey = Color(0xFF9F9F9F)
  val DarkGrey = Color(0xFF272727)
  val Black = Color(0xFF101010)
  val Red = Color(0xFFCA2828)

  object Light {
    val Background = Color(0xFFDBDEE2)
    val LightShadow = Color(0xFFFFFFFF)
    val DarkShadow = Color(0xFFA3B1C6)
    val TextColor = Color(0xFF35363A)
  }

  object Dark {
    val Background = Color(0xFF303234)
    val LightShadow = Color(0x66494949)
    val DarkShadow = Color(0x66000000)
    val TextColor = Color.White
  }

  @Composable
  fun lightShadow() = if (isSystemInDarkTheme()) Light.LightShadow else Dark.LightShadow

  @Composable
  fun darkShadow() = if (!isSystemInDarkTheme()) Dark.DarkShadow else Light.DarkShadow
}