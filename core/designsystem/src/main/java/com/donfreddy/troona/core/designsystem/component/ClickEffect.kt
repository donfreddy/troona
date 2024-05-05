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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

enum class ButtonState { Pressed, Idle }

fun Modifier.bounceClick() = composed {
  var buttonState by remember { mutableStateOf(ButtonState.Idle) }
  val scale by animateFloatAsState(
    if (buttonState == ButtonState.Pressed) 0.70f else 1f, label = ""
  )

  this
    .graphicsLayer {
      scaleX = scale
      scaleY = scale
    }
    .clickable(interactionSource = remember { MutableInteractionSource() },
      indication = null,
      onClick = { })
    .pointerInput(buttonState) {
      detectTapGestures(
        onPress = { buttonState = ButtonState.Pressed },
        onTap = { buttonState = ButtonState.Idle },
      )
    }


}