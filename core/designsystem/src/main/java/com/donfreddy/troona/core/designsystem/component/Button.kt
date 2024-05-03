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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.donfreddy.troona.core.designsystem.component.TroonaButtonDefaults.RippleRadius
import com.donfreddy.troona.core.designsystem.theme.TroonaTheme

/**
 * Troona filled button with generic content slot. Wraps Material [Button]
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun TroonaButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  content: @Composable RowScope.() -> Unit,
) {
  Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colors = ButtonDefaults.buttonColors(
      contentColor = MaterialTheme.colors.onBackground
    ),
    contentPadding = contentPadding,
    content = content,
  )
}

@Composable
fun TroonaButton(
) {
  Button(onClick = { /*TODO*/ }) {

  }
}

/**
 * Troona outlined button with generic content slot. Wraps Material [OutlinedButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun TroonaOutlinedButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  content: @Composable RowScope.() -> Unit,
) {
  OutlinedButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colors = ButtonDefaults.outlinedButtonColors(
      contentColor = MaterialTheme.colors.onBackground,
    ),
    border = BorderStroke(
      width = TroonaButtonDefaults.OutlinedButtonBorderWidth,
      color = if (enabled) {
        MaterialTheme.colors.onSecondary
      } else {
        MaterialTheme.colors.onSurface.copy(
          alpha = TroonaButtonDefaults.DISABLED_OUTLINED_BUTTON_BORDER_ALPHA,
        )
      },
    ),
    contentPadding = contentPadding,
    content = content,
  )
}

/**
 * Troona text button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 */
@Composable
fun TroonaTextButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  text: @Composable () -> Unit,
  leadingIcon: @Composable (() -> Unit)? = null,
) {
  // Todo: implement component here.
}

@Composable
fun TroonaIconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  rippleRadius: Dp = RippleRadius,
  rippleColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
  enabled: Boolean = true,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  content: @Composable () -> Unit,
) {
  Box(
    modifier = modifier
      .clickable(
        enabled = enabled,
        role = Role.Button,
        onClick = onClick,
        interactionSource = interactionSource,
        indication = rememberRipple(
          bounded = false,
          radius = rippleRadius,
          //color = rippleColor
        ),
      ),
    contentAlignment = Alignment.Center
  ) {
    val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
    CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
  }
}

@Preview
@Composable
fun NiaButtonPreview() {
  TroonaTheme {
    //
  }
}

/**
 * Troona button default values.
 */
object TroonaButtonDefaults {
  // OutlinedButton border color doesn't respect disabled state by default
  const val DISABLED_OUTLINED_BUTTON_BORDER_ALPHA = 0.12f

  // OutlinedButton default border width isn't exposed via ButtonDefaults
  val OutlinedButtonBorderWidth = 1.dp

  // Default ripple radius for TroonaIconButton
  val RippleRadius = 24.dp
}