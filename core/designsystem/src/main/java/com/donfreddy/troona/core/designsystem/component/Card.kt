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

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TroonaCard(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  shapeSize: Dp = CardShapeSize,
  clickedShapeSize: Dp = CardClickedShapeSize,
  containerColor: Color = MaterialTheme.colorScheme.surface,
  elevation: Dp = 0.dp,
  border: BorderStroke? = null,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  content: @Composable ColumnScope.() -> Unit
) {
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed) CardPressedScale else 1f,
    animationSpec = CardPressedAnimation,
    label = "ScaleAnimation"
  )
  val alpha by animateFloatAsState(
    targetValue = if (isPressed) CardPressedAlpha else 1f,
    animationSpec = CardPressedAnimation,
    label = "AlphaAnimation"
  )
  val animatedShapeSize by animateDpAsState(
    targetValue = if (isPressed) clickedShapeSize else shapeSize,
    label = "ShapeSizeAnimation"
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha)
      .indication(interactionSource = interactionSource, indication = null)
      .clip(MaterialTheme.shapes.large)
      .clickable(onClick = onClick),
    //elevation = elevation,
    //backgroundColor = containerColor,
    content = content
  )
}

private const val CardPressedScale = 0.95f
private const val CardPressedAlpha = 0.75f
private val CardPressedAnimation = tween<Float>()
private val CardShapeSize = 12.dp
private val CardClickedShapeSize = 16.dp