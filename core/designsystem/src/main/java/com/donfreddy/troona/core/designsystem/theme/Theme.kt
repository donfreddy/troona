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

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.donfreddy.troona.core.designsystem.theme.TroonaColor.PrimaryColor

private val lightScheme = lightColors(
  primary = PrimaryColor,
  //secondary = PurpleGrey40,
  surface = TroonaColor.Light.Background,
  onSurface = TroonaColor.Light.TextColor,
  background = TroonaColor.Light.Background,
  onBackground = TroonaColor.Light.TextColor,

  /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val darkScheme = darkColors(
  primary = PrimaryColor,
  // secondary = PurpleGrey80,
  // tertiary = Pink80
)

/**
 * Troona theme.
 *
 * @param isDarkTheme Whether the theme should use a dark color scheme (follows system by default).
 * @param content The content of the theme.
 */
@Composable
fun TroonaTheme(
  isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
      WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDarkTheme
    }
  }

  val configuration = LocalConfiguration.current
  val dimensions = if (configuration.screenWidthDp <= 360) smallDimensions else sw360Dimensions

  val dimensionSet = remember { dimensions }
  CompositionLocalProvider(
    LocalSpacing provides Spacing(), LocalDimens provides dimensionSet
  ) {
    MaterialTheme(
      colors = if (isDarkTheme) darkScheme else lightScheme,
      typography = TroonaTypography,
      shapes = TroonaShapes,
      content = content
    )
  }
}

/**
 * Spacing values used in the app
 */
val MaterialTheme.spacing: Spacing
  @Composable @ReadOnlyComposable get() = LocalSpacing.current

val MaterialTheme.dimens: Dimensions
  @Composable @ReadOnlyComposable get() = LocalDimens.current
