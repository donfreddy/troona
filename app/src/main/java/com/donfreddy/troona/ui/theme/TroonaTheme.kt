/*
=============
Author: Don Freddy
Github: https://github.com/donfreddy
Website: https://donfreddy.com
=============
Application: Troona - Music Player
Homepage: https://github.com/donfreddy/troona
License: https://github.com/donfreddy/troona/blob/main/LICENSE
Copyright: © 2023, Don Freddy. All rights reserved.
=============
*/

package com.donfreddy.troona.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.donfreddy.troona.ui.theme.TroonaColor.Purple40
import com.donfreddy.troona.ui.theme.TroonaColor.Purple80
import com.donfreddy.troona.ui.theme.TroonaColor.PurpleGrey40
import com.donfreddy.troona.ui.theme.TroonaColor.PurpleGrey80

private val troonaDarkColors = darkColors(
  primary = Purple80,
  secondary = PurpleGrey80,
)

private val troonaLightColors = lightColors(
  primary = Purple40,
  secondary = PurpleGrey40,

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

private val troonaShapes = Shapes(
  small = RoundedCornerShape(2.dp),
  medium = RoundedCornerShape(4.dp),
  large = RoundedCornerShape(6.dp)
)

@Composable
fun TroonaTheme(
  isDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val troonaColors = if (isDarkTheme) troonaDarkColors else troonaLightColors

  // Responsible screen sizes @see https://www.youtube.com/watch?v=Dj_X-RKL-c0

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      // window.statusBarColor = colorScheme.surface.toArgb() // here change the color
      // window.navigationBarColor = colorScheme.background.toArgb() // here change the color

      // here change the status bar element color
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
    }
  }

  MaterialTheme(
    colors = troonaColors,
    typography = troonaTypography,
    shapes = troonaShapes,
    content = content
  )
}