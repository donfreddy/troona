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

package com.donfreddy.troona.core.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

/**
 * Single Line Text. Wraps Material [Text]
 *
 * @param text The text label content.
 * @param modifier Modifier to be applied to the tab.
 * @param shouldUseMarquee Controls the enabled state of the tab. When `false`, this tab will not be
 * clickable and will appear disabled to accessibility services.
 * @param color The text label content.
 * @param style The text label content.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleLineText(
  text: String,
  modifier: Modifier = Modifier,
  shouldUseMarquee: Boolean = false,
  color: Color = Color.Unspecified,
  style: TextStyle = LocalTextStyle.current
){
  Text(
    modifier = modifier.then(if (shouldUseMarquee) Modifier.basicMarquee() else Modifier),
    text = text,
    color = color,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1,
    style = style
  )
}