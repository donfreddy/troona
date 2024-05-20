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

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.donfreddy.troona.core.designsystem.R

val Nunito = FontFamily(
  Font(R.font.nunito_light, FontWeight.W300),
  Font(R.font.nunito_regular, FontWeight.W400),
  Font(R.font.nunito_italic, FontWeight.W400, FontStyle.Italic),
  Font(R.font.nunito_medium, FontWeight.W500),
  Font(R.font.nunito_semibold, FontWeight.W600),
  Font(R.font.nunito_bold, FontWeight.W700)
)
