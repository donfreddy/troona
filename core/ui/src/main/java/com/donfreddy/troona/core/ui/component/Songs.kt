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

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.core.designsystem.component.SingleLineText
import com.donfreddy.troona.core.designsystem.component.TroonaCard
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.designsystem.images.TroonaArtwork
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.model.enums.AlbumSortBy
import com.donfreddy.troona.core.model.enums.ArtistSortBy
import com.donfreddy.troona.core.model.enums.SongSortBy
import com.donfreddy.troona.core.model.enums.SortOrder
import com.donfreddy.troona.core.ui.R
import com.donfreddy.troona.core.ui.component.song.asDuration

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Songs(
  songs: List<Song>,
  currentPlayingSongId: String,
  onClick: (Int) -> Unit,
  modifier: Modifier = Modifier,
  context: Context = LocalContext.current
) {
  val dropdownItems = listOf(
    DropDownItem("Ascending", false),
    DropDownItem("Descending", false),
    DropDownItem("Artist", false),
    DropDownItem("Album", false),
    DropDownItem("Year", false),
    DropDownItem("Date Added", true),
    DropDownItem("Date Modified", false),
    DropDownItem("Composer", false),
  )

  LazyColumn(modifier = modifier.fillMaxSize()) {

    if (songs.isNotEmpty()) {
      val totalSongs = context.resources.getQuantityString(
        R.plurals.core_ui_number_of_songs, songs.size, songs.size
      )
      item {
        MediaHeader(
          sortParams = SortParams(
            sortOrder = SortOrder.Ascending,
            songSortBy = SongSortBy.Title,
            artistSortBy = ArtistSortBy.NumberOfSongs,
            albumSortBy = AlbumSortBy.Artist,
            onChangeSortOrder = {},
            onChangeSongSortBy = {},
            onChangeArtistSortBy = {},
            onChangeAlbumSortBy = {},
          ),
          itemCountString = totalSongs,
          dropdownItems = dropdownItems,
        )
        TroonaDivider(
          Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.small)
            .padding(bottom = MaterialTheme.spacing.extraSmall)
        )
      }

      itemsIndexed(items = songs, key = { _, song -> song.id }) { index, song ->
        SongItem(modifier = modifier.animateItemPlacement(),
          song = song,
          isPlaying = song.id.toString() == currentPlayingSongId,
          onClick = { onClick(index) },
          onMoreClick = {})
      }
    } else {
      item {
        EmptyContent(textResource = R.string.core_ui_no_artists)
      }
    }
  }
}

@Composable
private fun SongItem(
  song: Song,
  isPlaying: Boolean,
  onClick: () -> Unit,
  onMoreClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val backgroundColor =
    if (isPlaying) MaterialTheme.colors.primary.copy(alpha = 0.08f) else MaterialTheme.colors.surface
  val textColor = if (isPlaying) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface

  Row(
    modifier = Modifier
      .clickable(
        onClick = onClick,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(
          bounded = false, color = MaterialTheme.colors.primary.copy(alpha = 0.2f)
        )
      )
      .background(color = backgroundColor)
      .padding(
        horizontal = MaterialTheme.spacing.small,
        vertical = MaterialTheme.spacing.extraSmall
      )
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      modifier = Modifier.weight(SongDescriptionWeight),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium),
      verticalAlignment = Alignment.CenterVertically
    ) {
      TroonaArtwork(
        modifier = Modifier.size(SongCoverSize),
        artworkUri = song.albumArt,
        contentDescription = song.title
      )
      Column(verticalArrangement = Arrangement.Center) {
        SingleLineText(
          text = song.title,
          shouldUseMarquee = isPlaying,
          style = MaterialTheme.typography.body1.copy(
            fontSize = 20.sp, color = textColor
          )
        )
        SingleLineText(
          text = "${song.artistName} • ${song.duration.asDuration()}",
          shouldUseMarquee = isPlaying,
          style = MaterialTheme.typography.body2,
          color = TroonaColor.Grey
        )
      }
    }
    IconButton(onClick = onMoreClick) {
      Icon(
        painter = painterResource(id = TroonaIcons.MoreHorizontal.resourceId),
        tint = textColor,
        contentDescription = "More",
      )
    }
  }
}

private const val SongDescriptionWeight = 0.9f
private const val FavoriteButtonWeight = 0.1f
private val SongCoverSize = 45.dp

@Preview
@Composable
private fun SongItemPreview() {
  SongItem(song = Song.EXAMPLE, isPlaying = false, onClick = {}, onMoreClick = {})
}