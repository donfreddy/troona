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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.core.designsystem.component.SingleLineText
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.designsystem.images.TroonaArtwork
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.model.data.Artist
import com.donfreddy.troona.core.model.enums.AlbumSortBy
import com.donfreddy.troona.core.model.enums.ArtistSortBy
import com.donfreddy.troona.core.model.enums.SongSortBy
import com.donfreddy.troona.core.model.enums.SortOrder
import com.donfreddy.troona.core.ui.R
import com.donfreddy.troona.core.ui.component.song.asDuration
import com.donfreddy.troona.core.ui.util.HelpersUtil.getArtistInfoString

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Artists(
  artists: List<Artist>,
  onClick: (Long) -> Unit,
  modifier: Modifier = Modifier,
  context: Context = LocalContext.current
) {
  val dropdownItems = listOf(
    DropDownItem("Ascending", false),
    DropDownItem("Descending", false),
    DropDownItem("Artist", false),
    DropDownItem("Number of Songs", true),
    DropDownItem("Number of Albums", false),
  )

  LazyColumn(modifier = modifier.fillMaxSize()) {

    if (artists.isNotEmpty()) {
      val totalArtists = context.resources.getQuantityString(
        R.plurals.core_ui_number_of_artists, artists.size, artists.size
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
          itemCountString = totalArtists,
          dropdownItems = dropdownItems,
        )
        TroonaDivider(
          Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.small)
            .padding(bottom = MaterialTheme.spacing.extraSmall)
        )
      }

      items(items = artists, key = Artist::id) { artist ->
        ArtistItem(modifier = modifier.animateItemPlacement(),
          artist = artist,
          onClick = { onClick(artist.id) },
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
private fun ArtistItem(
  artist: Artist,
  onClick: () -> Unit,
  onMoreClick: () -> Unit,
  modifier: Modifier = Modifier,
  context: Context = LocalContext.current
) {
  Row(
    modifier = Modifier
      .clickable(
        onClick = onClick,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(
          bounded = false, color = MaterialTheme.colors.primary.copy(alpha = 0.2f)
        )
      )
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
        shape = RoundedCornerShape(50.dp),
        artworkUri = artist.albums.first().songs.first().albumArt,
        contentDescription = artist.name
      )
      Column(verticalArrangement = Arrangement.Center) {
        SingleLineText(
          text = artist.name,
          style = MaterialTheme.typography.body1.copy(
            fontSize = 20.sp,
          )
        )
        SingleLineText(
          text = getArtistInfoString(context = context, artist = artist),
          style = MaterialTheme.typography.body2,
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

private const val SongDescriptionWeight = 0.9f
private val SongCoverSize = 45.dp