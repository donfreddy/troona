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

package com.donfreddy.troona.core.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.donfreddy.troona.core.designsystem.component.TroonaButton
import com.donfreddy.troona.core.designsystem.theme.Nunito
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import com.donfreddy.troona.core.designsystem.theme.spacing

@Composable
fun PlayOrShuffleButtons(
  onPlay: () -> Unit,
  onShuffle: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.padding(horizontal = MaterialTheme.spacing.medium),
  ) {
    TroonaButton(
      modifier = Modifier.weight(1f),
      onClick = onPlay,
    ) {
      Text(
        text = "Play", style = MaterialTheme.typography.labelLarge.copy(
          fontFamily = Nunito, fontWeight = FontWeight.W600, color = TroonaColor.White
        )
      )
    }
    Spacer(modifier = Modifier.weight(0.1f))
    FilledTonalButton(
      modifier = Modifier.weight(1f),
      colors = ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
      ),
      onClick = onShuffle,
    ) {
      Text(
        text = "Shuffle", style = MaterialTheme.typography.labelLarge.copy(
          fontFamily = Nunito,
          fontWeight = FontWeight.W600,
          color = MaterialTheme.colorScheme.primary
        )
      )
    }
  }
}

