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

package com.donfreddy.troona.feature.album

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.donfreddy.troona.core.designsystem.component.TroonaTopBar
import com.donfreddy.troona.core.designsystem.images.TroonaArtwork
import com.donfreddy.troona.core.designsystem.theme.Nunito
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.model.data.Album
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.ui.common.SongCoverSize
import com.donfreddy.troona.core.ui.component.PlayOrShuffleButtons
import com.donfreddy.troona.core.ui.component.SongItem
import com.donfreddy.troona.core.ui.component.TroonaDivider
import com.donfreddy.troona.core.ui.util.HelpersUtil.getAlbumInfoStringWithDuration

@UnstableApi
@Composable
internal fun AlbumScreen(
  onBackClick: () -> Unit,
  onAlbumClick: (Long) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: AlbumViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val audioState by viewModel.audioState.collectAsStateWithLifecycle()

  when (val uiState = state) {
    AlbumUiState.Loading -> Unit

    is AlbumUiState.Success -> {
      AlbumScreen(
        album = uiState.album,
        otherAlbums = uiState.otherAlbums,
        onBackClick = onBackClick,
        onSongClick = { startIndex ->
          (viewModel::play)(uiState.album.songs, startIndex)
        },
        onAlbumClick = onAlbumClick,
        currentPlayingSongId = audioState.currentMediaId,
        modifier = modifier
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumScreen(
  album: Album,
  otherAlbums: List<Album>,
  onBackClick: () -> Unit,
  onSongClick: (Int) -> Unit,
  onAlbumClick: (Long) -> Unit,
  currentPlayingSongId: String,
  modifier: Modifier = Modifier,
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  var isContextual by remember { mutableStateOf(false) }

  Scaffold(
    Modifier
      .fillMaxSize()
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TroonaTopBar(
        searchWidgetState = { /*TODO*/ },
        onBackClick = onBackClick,
        scrollBehavior = scrollBehavior,
      )
    },

    ) { innerPadding ->
    val scrollState = rememberScrollState()

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(scrollState),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
      AlbumHeaderSection(album = album)

      PlayOrShuffleButtons(
        onPlay = { isContextual = !isContextual },
        onShuffle = { /*TODO*/ },
      )

      TroonaDivider()

      AlbumSongsSection(songs = album.songs,
        currentPlayingSongId = currentPlayingSongId,
        onSongClick = onSongClick,
        onMoreClick = { /*TODO*/ })

      if (otherAlbums.isNotEmpty()) {
        OtherArtistAlbumsSection(albums = otherAlbums, onAlbumClick = onAlbumClick)
      }

      Spacer(modifier = Modifier.height(100.dp))
    }
  }
}

@Composable
fun AlbumHeaderSection(
  album: Album, context: Context = LocalContext.current
) {
  TroonaArtwork(
    modifier = Modifier
      .size(SongCoverSize.MEDIUM.value)
      .padding(top = MaterialTheme.spacing.extraSmall),
    artworkUri = album.songs.first().albumArt,
    shape = MaterialTheme.shapes.large,
    contentDescription = album.artistName,
  )
  Column {
    Text(
      text = album.title,
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(horizontal = MaterialTheme.spacing.extraSmall),
      style = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Bold, fontFamily = Nunito
      ),
      textAlign = TextAlign.Center
    )
    Text(
      text = album.artistName,
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(horizontal = MaterialTheme.spacing.extraSmall),
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        fontFamily = Nunito,
        color = MaterialTheme.colorScheme.primary
      ),
      textAlign = TextAlign.Center
    )
    Text(
      text = getAlbumInfoStringWithDuration(context, album),
      modifier = Modifier.align(Alignment.CenterHorizontally),
      style = MaterialTheme.typography.titleMedium.copy(
        color = Color.Gray, fontFamily = Nunito, fontWeight = FontWeight.W600
      )
    )
  }
}

@Composable
fun AlbumSongsSection(
  songs: List<Song>,
  currentPlayingSongId: String,
  onSongClick: (Int) -> Unit,
  onMoreClick: () -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
    ) {
      Text(
        text = "Songs",
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          //fontWeight = FontWeight.W600,
          fontFamily = Nunito
        ),
      )
    }
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = MaterialTheme.spacing.small)
        .padding(horizontal = MaterialTheme.spacing.small),
    ) {
      songs.forEachIndexed { index, song ->
        SongItem(
          song = song,
          isPlaying = song.id.toString() == currentPlayingSongId,
          onClick = { onSongClick(index) },
          onMoreClick = onMoreClick
        )
      }
    }
  }
}

@Composable
fun OtherArtistAlbumsSection(
  albums: List<Album>, onAlbumClick: (Long) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
    ) {
      Text(
        text = "Plus de ${albums.first().artistName}",
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          //fontWeight = FontWeight.W600,
          fontFamily = Nunito
        ),
      )
    }
    LazyRow {
      items(items = albums, key = Album::id) { album ->
        val isFist = albums.indexOf(album) == 0
        val isLast = albums.indexOf(album) == albums.size - 1
        Column(
          modifier = Modifier
            .padding(
              start = if (isFist) MaterialTheme.spacing.medium else MaterialTheme.spacing.small,
              end = if (isLast) MaterialTheme.spacing.medium else MaterialTheme.spacing.small,
              top = MaterialTheme.spacing.medium,
            )
            .clickable(onClick = { onAlbumClick(album.id) })
        ) {
          TroonaArtwork(
            modifier = Modifier.size(SongCoverSize.LARGE.value / 2),
            artworkUri = album.songs.first().albumArt,
            shape = MaterialTheme.shapes.medium,
            contentDescription = album.artistName,
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
          Text(
            text = album.title,
            modifier = Modifier.width(150.dp),
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.W600, fontFamily = Nunito
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}


@Preview(showBackground = true, name = "Favorites screen")
@Composable
fun FavoritesScreenPreview() {
  AlbumScreen(
    album = Album.empty,
    otherAlbums = emptyList(),
    onBackClick = {},
    onSongClick = {},
    onAlbumClick = {},
    currentPlayingSongId = ""
  )
}