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

package com.donfreddy.troona.core.ui.song

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.donfreddy.troona.core.designsystem.component.SingleLineText
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
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(CardShapeSize))
      .clickable(onClick = onClick),
    //backgroundColor = Color.Transparent,
    shape = RoundedCornerShape(CardShapeSize)
  ) {
    Row(
      modifier = Modifier
        .padding(
          //horizontal = MaterialTheme.spacing.small,
          //vertical = MaterialTheme.spacing.extraSmall
        )
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.weight(0.9f),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium),
        verticalAlignment = Alignment.CenterVertically
      ) {
        TroonaArtwork(
          modifier = Modifier.size(45.dp),
          artworkUri = song.albumArt,
          contentDescription = ""
        )
        Column {
          SingleLineText(
            text = song.title,
            style = MaterialTheme.typography.subtitle1.copy(
              fontWeight = FontWeight.SemiBold
            ),
            shouldUseMarquee = isPlaying

          )
          SingleLineText(
            text = "${song.artistName} • ${song.duration.asDuration()}",
            style = MaterialTheme.typography.body2,
            shouldUseMarquee = isPlaying,
            color = TroonaColor.Grey
          )
        }
      }
      IconButton(
        onClick = { },
        modifier = Modifier
      ) {
        Icon(Icons.Default.MoreVert, contentDescription = null)
      }
    }
  }
}

private val CardShapeSize = 12.dp

internal fun Long.asDuration(): String {
  val hours = this / (1000 * 60 * 60)
  val minutes = (this % (1000 * 60 * 60)) / (1000 * 60)
  val seconds = (this % (1000 * 60)) / 1000

  return if (hours > 0) {
    String.format(locale = null, format = "%d:%02d:%02d", hours, minutes, seconds)
  } else {
    // String.format(locale = null, format = "%02d:%02d", minutes, seconds) // 03:10
    String.format(locale = null, format = "%d:%02d", minutes, seconds) // 3:10
  }
}