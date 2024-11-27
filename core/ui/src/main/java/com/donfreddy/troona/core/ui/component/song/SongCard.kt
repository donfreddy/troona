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

package com.donfreddy.troona.core.ui.component.song

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.core.common.extensions.toDurationString
import com.donfreddy.troona.core.designsystem.component.SingleLineText
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.designsystem.images.TroonaArtwork
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.model.data.Song

/**
 * [Song] card used on the following screens: For You, Saved
 */
@Composable
fun SongCard(
  song: Song,
  isPlaying: Boolean,
  onClick: () -> Unit,
  onMoreClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
     .padding(horizontal = MaterialTheme.spacing.small)
      .clip(MaterialTheme.shapes.medium)
      .clickable(onClick = onClick),
    shape = MaterialTheme.shapes.medium,
   // elevation = 0.dp,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.weight(0.9f),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
      ) {
        TroonaArtwork(
          modifier = Modifier.size(MaterialTheme.spacing.extraLarge),
          artworkUri = song.albumArt,
          contentDescription = song.title
        )
        Column(verticalArrangement = Arrangement.Center) {
          SingleLineText(
            text = song.title,
            shouldUseMarquee = isPlaying,
            fontSize = 15.sp,
            color = if (isPlaying) MaterialTheme.colorScheme.primary else Color.Unspecified
          )
          SingleLineText(
            text = "${song.artistName} • ${song.duration.toDurationString()}",
            shouldUseMarquee = isPlaying,
            fontSize = 12.sp,
            color = TroonaColor.Grey
          )
        }
      }
      IconButton(onClick = onMoreClick) {
        Icon(
          painter = painterResource(id = TroonaIcons.MoreHorizontal.resourceId),
          contentDescription = "More",
        )
      }
    }
  }
}

