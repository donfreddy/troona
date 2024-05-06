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

package com.donfreddy.troona.feature.player.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.feature.player.util.asFormattedString
import com.donfreddy.troona.feature.player.util.convertToProgress

@Composable
fun SeekBar(
  currentPosition: Long,
  duration: Long,
  onSkipTo: (Float) -> Unit,
  modifier: Modifier = Modifier,
) {

  val progress by animateFloatAsState(
    targetValue = convertToProgress(count = currentPosition, total = duration),
    label = "ProgressAnimation"
  )

  Column(
    modifier = modifier
      .padding(vertical = MaterialTheme.spacing.medium)
      .fillMaxWidth(),
  ) {
    TroonaSlider(
      value = progress,
      onValueChanged = onSkipTo,
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
    Row(
      modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = currentPosition.asFormattedString(),
        style = MaterialTheme.typography.body2.copy(
          fontWeight = FontWeight.W700,
          fontSize = 11.sp,
          color = TroonaColor.WhiteAlpha04,
        ),
      )
      Text(
        text = "−${(duration - currentPosition).asFormattedString()}",
        style = MaterialTheme.typography.body2.copy(
          fontWeight = FontWeight.W700,
          fontSize = 11.sp,
          color = TroonaColor.WhiteAlpha04,
        ),
      )
    }
  }
}