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

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.R

private val light = Font(R.font.nunito_light, FontWeight.W300)
private val regular = Font(R.font.nunito_regular, FontWeight.W400)
private val italic = Font(R.font.nunito_italic, FontWeight.W400, FontStyle.Italic)
private val medium = Font(R.font.nunito_medium, FontWeight.W500)
private val semiBold = Font(R.font.nunito_semibold, FontWeight.W600)
private val bold = Font(R.font.nunito_bold, FontWeight.W700)

private val troonaFontFamily = FontFamily(light, regular, italic, medium, semiBold, bold)

// Set of Material typography styles to start with
val troonaTypography = Typography(
  defaultFontFamily = troonaFontFamily,

  /*h1 = TextStyle(
    fontFamily = troonaFontFamily,
    fontWeight = FontWeight.W300,
    fontSize = 96.sp
  ),
  h2 = TextStyle(
    fontFamily = troonaFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 60.sp
  ),
  h3 = TextStyle(
    fontFamily = troonaFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 48.sp
  ),
  h4 = TextStyle(
    fontFamily = troonaFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 34.sp
  ),
  h5 = TextStyle(
    fontFamily = troonaFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 24.sp
  ),
  h6 = TextStyle(
    fontFamily = troonaFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 20.sp
  ),
  subtitle1 = TextStyle(
    fontFamily = troonaFontFamily,
    fontWeight = FontWeight.W500,
    fontSize = 16.sp
  ),
  subtitle2 = TextStyle(
    fontFamily = troonaFontFamily,
    fontWeight = FontWeight.W600,
    fontSize = 14.sp
  ),*/
  body1 = TextStyle(
    fontFamily = troonaFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
  ),
  /* body2 = TextStyle(
     fontFamily = troonaFontFamily,
     fontWeight = FontWeight.W400,
     fontSize = 14.sp
   ),
   button = TextStyle(
     fontFamily = troonaFontFamily,
     fontWeight = FontWeight.W600,
     fontSize = 14.sp
   ),
   caption = TextStyle(
     fontFamily = troonaFontFamily,
     fontWeight = FontWeight.W500,
     fontSize = 12.sp
   ),
   overline = TextStyle(
     fontFamily = troonaFontFamily,
     fontWeight = FontWeight.W400,
     fontSize = 12.sp
   )*/
)

object TroonaStyle {
  private val nunitoStyle: TextStyle
    @Composable get() = TextStyle(fontFamily = troonaFontFamily)

  val nunitoNormal14: TextStyle
    @Composable get() = nunitoStyle.copy(fontWeight = FontWeight.W400, fontSize = 14.sp)

  val h1: TextStyle
    @Composable get() = MaterialTheme.typography.h1

  val h2: TextStyle
    @Composable get() = MaterialTheme.typography.h2

  val h3: TextStyle
    @Composable get() = MaterialTheme.typography.h3

  val h4: TextStyle
    @Composable get() = MaterialTheme.typography.h4

  val h5: TextStyle
    @Composable get() = MaterialTheme.typography.h5

  val h6: TextStyle
    @Composable get() = MaterialTheme.typography.h6

  val subtitle1: TextStyle
    @Composable get() = MaterialTheme.typography.subtitle1

  val subtitle2: TextStyle
    @Composable get() = MaterialTheme.typography.subtitle2

  val body1: TextStyle
    @Composable get() = MaterialTheme.typography.body1

  val body2: TextStyle
    @Composable get() = MaterialTheme.typography.body2

  val button: TextStyle
    @Composable get() = MaterialTheme.typography.button

  val caption: TextStyle
    @Composable get() = MaterialTheme.typography.caption

  val overline: TextStyle
    @Composable get() = MaterialTheme.typography.overline
}