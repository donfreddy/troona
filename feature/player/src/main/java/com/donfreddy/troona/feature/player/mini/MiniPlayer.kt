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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donfreddy.troona.core.designsystem.component.SingleLineText
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.designsystem.images.TroonaArtwork
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.ui.song.asDuration
import com.donfreddy.troona.feature.player.PlayerViewModel
import com.donfreddy.troona.feature.player.UIEvents

@Composable
fun MiniPlayer(
  onNavigateToPlayer: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: PlayerViewModel = hiltViewModel(),
) {

  MiniPlayerContent(
    currentSong = viewModel.currentPlayingSong,
    isPlaying = viewModel.isPlaying,
    progress = viewModel.progress,
    onNavigateToPlayer = onNavigateToPlayer,
    onSkipNext = { viewModel.onUiEvent(UIEvents.SeekToNext) },
    onPlayPause = { viewModel.onUiEvent(UIEvents.PlayPause) },
    modifier = modifier
  )
}

@Composable
private fun MiniPlayerContent(
  currentSong: Song,
  isPlaying: Boolean,
  progress: Float,
  onNavigateToPlayer: () -> Unit,
  onSkipNext: () -> Unit,
  onPlayPause: () -> Unit,
  modifier: Modifier = Modifier,
) {
  AnimatedVisibility(
    visible = true,
    enter = slideInVertically(initialOffsetY = { it }),
    exit = slideOutVertically(targetOffsetY = { it }),
  ) {
    Surface(elevation = MaterialTheme.spacing.extraSmall) {
      Column(
        modifier =
        modifier
          .clickable(onClick = onNavigateToPlayer)

      ) {
        Row(
          modifier = Modifier
            .padding(
              horizontal = MaterialTheme.spacing.small,
              vertical = MaterialTheme.spacing.extraSmall
            )
            .fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
          ) {
            Box {
              TroonaArtwork(
                modifier = Modifier.size(MaterialTheme.spacing.extraLarge),
                artworkUri = currentSong.albumArt,
                contentDescription = currentSong.title
              )
            }
            Column(verticalArrangement = Arrangement.Center) {
              SingleLineText(
                text = currentSong.title,
                shouldUseMarquee = isPlaying,
                fontSize = 14.sp,
              )
              SingleLineText(
                text = "${currentSong.artistName} • ${currentSong.duration.asDuration()}",
                shouldUseMarquee = isPlaying,
                fontSize = 12.sp,
                color = TroonaColor.Grey
              )
            }
          }

          Row {
            IconButton(onClick = onPlayPause) {
              Icon(
                painter = painterResource(id = if (isPlaying) TroonaIcons.Pause.resourceId else TroonaIcons.Play.resourceId),
                contentDescription = "Play/Pause",
                modifier = Modifier.size(30.dp)
              )
            }
            IconButton(onClick = onSkipNext) {
              Icon(
                painter = painterResource(id = TroonaIcons.FastForward.resourceId),
                contentDescription = "Skip Next",
                modifier = Modifier.size(30.dp)
              )
            }
          }
        }
        LinearProgressIndicator(
          modifier = Modifier
            .fillMaxWidth()
            .height(3.dp),
          progress = progress
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MiniPlayerContentPreview() {
  MiniPlayerContent(
    currentSong = Song.EXAMPLE,
    isPlaying = true,
    progress = 0.5f,
    modifier = Modifier.fillMaxWidth(),
    onNavigateToPlayer = {},
    onSkipNext = {},
    onPlayPause = {}
  )
}
