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

package com.donfreddy.troona.feature.player

import android.view.MotionEvent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import kotlin.math.abs

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TroonaSlider(
  value: Float,
  onValueChanged: (Float) -> Unit,
  modifier: Modifier = Modifier,
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
  stepSize: Float = 0.01f,
  enabled: Boolean = true,
) {
  var pressed by remember { mutableStateOf(false) }
  val radius by animateDpAsState(
    targetValue = if (pressed) 10.dp else 5.dp,
    label = "DpAnimation",
  )
  var canvasSize by remember { mutableStateOf(Size.Zero) }
  var downX by remember { mutableFloatStateOf(0f) }

  val trackStrokeWidth: Float
  val thumbPx: Float
  with(LocalDensity.current) {
    trackStrokeWidth = TrackHeight.toPx()
    thumbPx = radius.toPx()
  }

  Canvas(modifier = modifier.pointerInteropFilter { event ->
    val range = valueRange.endInclusive - valueRange.start
    val threshold = canvasSize.width / (range / stepSize)

    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        if (!enabled) return@pointerInteropFilter false

        val p = Offset(
          (value - valueRange.start) / (valueRange.endInclusive - valueRange.start) * canvasSize.width,
          canvasSize.height / 2f
        )

        if (event.x in (p.x - thumbPx)..(p.x - thumbPx) && event.y in (p.y - thumbPx)..(p.y - thumbPx)) {
          pressed = true
          downX = event.x
          true
        } else false
      }

      MotionEvent.ACTION_MOVE -> {
        val dx = event.x - downX
        if (abs(dx) >= threshold) {
          val newValue = if (dx > 0) value + stepSize else value - stepSize
          if (newValue in valueRange) {
            onValueChanged(newValue)
            downX = event.x
          }
        }
        true
      }

      MotionEvent.ACTION_UP -> {
        pressed = false
        downX = 0f
        true
      }

      else -> false
    }
  }) {
    canvasSize = size
    val sliderStart = Offset(0f, center.y)
    val sliderEnd = Offset(size.width, center.y)

    drawLine(
      color = TroonaColor.WhiteAlpha02,
      start = sliderStart,
      end = sliderEnd,
      strokeWidth = trackStrokeWidth,
      cap = StrokeCap.Round,
    )

    val sliderValue = Offset(
      (value - valueRange.start) / (valueRange.endInclusive - valueRange.start) * size.width,
      center.y
    )

    drawLine(
      color = TroonaColor.White,
      start = sliderStart,
      end = sliderValue,
      strokeWidth = trackStrokeWidth,
      cap = StrokeCap.Round,
    )

    drawCircle(TroonaColor.White, thumbPx, sliderValue)
  }
}

private val TrackHeight = 4.dp
