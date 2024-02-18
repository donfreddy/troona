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

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.core.designsystem.R

private val light = Font(R.font.nunito_light, FontWeight.W300)
private val regular = Font(R.font.nunito_regular, FontWeight.W400)
private val italic = Font(R.font.nunito_italic, FontWeight.W400, FontStyle.Italic)
private val medium = Font(R.font.nunito_medium, FontWeight.W500)
private val semiBold = Font(R.font.nunito_semibold, FontWeight.W600)
private val bold = Font(R.font.nunito_bold, FontWeight.W700)

private val troonaFontFamily = FontFamily(light, regular, italic, medium, semiBold, bold)

/**
 * Troona typography.
 */
internal val TroonaTypography = Typography(
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