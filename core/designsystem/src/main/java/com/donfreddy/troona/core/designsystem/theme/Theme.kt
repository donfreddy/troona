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

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp
import com.donfreddy.troona.core.designsystem.theme.TroonaColor.PrimaryColor

private val DarkColorScheme = darkColors(
  primary = PrimaryColor,
  // secondary = PurpleGrey80,
  // tertiary = Pink80
)

private val LightColorScheme = lightColors(
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

private val TroonaShapes = Shapes(
  small = RoundedCornerShape(2.dp),
  medium = RoundedCornerShape(4.dp),
  large = RoundedCornerShape(6.dp)
)

@Composable
fun TroonaTheme(
  isDarkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  useDynamicColor: Boolean = true,
  content: @Composable () -> Unit
) {
  val troonaColors = if (useDynamicColor && supportsDynamicTheming()) {
    TODO() // Dynamic theming
    // val context = LocalContext.current
    // if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
  } else {
    if (isDarkTheme) DarkColorScheme else LightColorScheme
  }

  CompositionLocalProvider(
    LocalSpacing provides Spacing()
  ) {
    MaterialTheme(
      colors = troonaColors,
      typography = Typography,
      shapes = TroonaShapes,
      content = content
    )
  }
}

/**
 * Spacing values used in the app
 */
val MaterialTheme.spacing: Spacing
  @Composable
  @ReadOnlyComposable
  get() = LocalSpacing.current

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S