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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.core.designsystem.component.SingleLineText
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.designsystem.images.TroonaArtwork
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.model.data.Album
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.model.enums.AlbumSortBy
import com.donfreddy.troona.core.model.enums.ArtistSortBy
import com.donfreddy.troona.core.model.enums.SongSortBy
import com.donfreddy.troona.core.model.enums.SortOrder
import com.donfreddy.troona.core.ui.R
import com.donfreddy.troona.core.ui.component.song.asDuration
import com.donfreddy.troona.core.ui.util.HelpersUtil.getArtistInfoString
import com.donfreddy.troona.core.ui.util.HelpersUtil.getTotalSongsString

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Albums(
  albums: List<Album>,
  onClick: (Long) -> Unit,
  modifier: Modifier = Modifier,
  context: Context = LocalContext.current
) {
  val dropdownItems = listOf(
    DropDownItem("Ascending", false),
    DropDownItem("Descending", false),
    DropDownItem("Album", false),
    DropDownItem("Artist", false),
    DropDownItem("Number of Songs", true),
  )

  LazyColumn(modifier = modifier.fillMaxSize()) {

    if (albums.isNotEmpty()) {
      val totalAlbums = context.resources.getQuantityString(
        R.plurals.core_ui_number_of_albums, albums.size, albums.size
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
          itemCountString = totalAlbums,
          dropdownItems = dropdownItems,
        )
        TroonaDivider(
          Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.small)
            .padding(bottom = MaterialTheme.spacing.extraSmall)
        )
      }

      item {
        LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          modifier = Modifier.height(900.dp),
          userScrollEnabled = false,
        ) {
          items(items = albums, key = Album::id) { album ->
            AlbumItem(
              modifier = Modifier.animateItemPlacement(),
              album = album,
              onClick = { onClick(album.id) },
              onMoreClick = {},
            )
          }
        }
      }
    } else {
      item {
        EmptyContent(textResource = R.string.core_ui_no_albums)
      }
    }
  }
}

@Composable
private fun AlbumItem(
  album: Album,
  onClick: () -> Unit,
  onMoreClick: () -> Unit,
  modifier: Modifier = Modifier,
  context: Context = LocalContext.current
) {
  Column(
    modifier = Modifier
      .clickable(
        onClick = onClick,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(
          bounded = false, color = MaterialTheme.colors.primary.copy(alpha = 0.2f)
        )
      )
      .padding(MaterialTheme.spacing.small)
      .fillMaxWidth(),
  ) {
    TroonaArtwork(
      modifier = Modifier.fillMaxSize(),
      shape = RoundedCornerShape(20.dp),
      artworkUri = album.songs.first().albumArt,
      contentDescription = album.title
    )
    Row(
      modifier = Modifier.height(26.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      SingleLineText(
        modifier = Modifier.weight(0.8f),
        text = album.title,
        style = MaterialTheme.typography.h5.copy(
          fontWeight = FontWeight.Bold,
        )
      )
      IconButton(onClick = onMoreClick, modifier = Modifier.weight(0.2f)) {
        Icon(
          painter = painterResource(id = TroonaIcons.MoreHorizontal.resourceId),
          contentDescription = "More",
        )
      }
    }
    SingleLineText(
      text = "${album.artistName} • ${album.year}", style = MaterialTheme.typography.body2.copy(
        color = TroonaColor.Grey
      )
    )
    SingleLineText(
      text = getTotalSongsString(context = context, numberOfSongs = album.songs.size),
      style = MaterialTheme.typography.body2.copy(
        color = TroonaColor.Grey
      )
    )
  }
}

@Preview
@Composable
private fun AlbumItemPreview() {
  AlbumItem(album = Album(id = -1, songs = listOf(Song.EXAMPLE)), onClick = {}, onMoreClick = {})
}

private const val SongDescriptionWeight = 0.9f
private val SongCoverSize = 45.dp