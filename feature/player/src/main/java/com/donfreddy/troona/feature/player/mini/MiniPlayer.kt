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

package com.donfreddy.troona.feature.player.mini

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MiniPlayer(
  modifier: Modifier = Modifier,
  onNavigateToPlayer: () -> Unit = {},
) {
  AnimatedVisibility(
    visible = true,
    enter = slideInVertically(initialOffsetY = { it }),
    exit = slideOutVertically(targetOffsetY = { it }),
  ) {
    Column(
      modifier = modifier
        //.background(color = Color.Red)
        .clickable { onNavigateToPlayer() }
    ) {
      Row(
        modifier = Modifier
          .padding(4.dp)
          .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
       horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box {
          Text(
            text = "Artwork",
            textAlign = TextAlign.Center,
            color = Color.White,
          )
        }
        Column {
          Text(
            text = "Song Title",
            textAlign = TextAlign.Center,
            color = Color.White,
          )
          Text(
            text = "Artist Name",
            textAlign = TextAlign.Center,
            color = Color.White,
          )
        }
      }
      //Todo: Add mini player controls
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MiniPlayerPreview() {
  MiniPlayer(modifier = Modifier.fillMaxWidth())
}
