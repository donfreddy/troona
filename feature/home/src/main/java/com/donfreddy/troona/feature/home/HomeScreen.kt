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

package com.donfreddy.troona.feature.home

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.donfreddy.troona.core.designsystem.component.TroonaTopBar
import com.donfreddy.troona.core.model.enums.AlbumSortBy
import com.donfreddy.troona.core.model.enums.ArtistSortBy
import com.donfreddy.troona.core.model.enums.SongSortBy
import com.donfreddy.troona.core.model.enums.SortOrder
import com.donfreddy.troona.core.ui.component.MediaPager
import com.donfreddy.troona.core.ui.component.SortParams
import timber.log.Timber

@OptIn(UnstableApi::class)
@Composable
internal fun HomeScreen(
  onArtistClick: (Long) -> Unit,
  onAlbumClick: (Long) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val audioState by viewModel.audioState.collectAsStateWithLifecycle()

  when (val uiState = state) {
    HomeUiState.Loading -> {
      Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        CircularProgressIndicator(color = MaterialTheme.colors.primary, strokeWidth = 5.dp)
      }
    }

    is HomeUiState.Success -> {
      HomeScreen(
        uiState = uiState,
        sortParams = SortParams(
          sortOrder = SortOrder.Ascending,
          songSortBy = SongSortBy.Title,
          artistSortBy = ArtistSortBy.NumberOfSongs,
          albumSortBy = AlbumSortBy.Artist,
          onChangeSortOrder = {},
          onChangeSongSortBy = viewModel::onChangeSongSortBy,
          onChangeArtistSortBy = {},
          onChangeAlbumSortBy = {},
        ),
        onSongClick = { startIndex ->
          Timber.tag("HomeRoute").d("onSongClick: %s", startIndex)
          (viewModel::play)(uiState.songs, startIndex)
        },
        onArtistClick = {
          onArtistClick(it)
          //Toast.makeText(context, "Artist: ${it}", Toast.LENGTH_SHORT).show()
        },
        onAlbumClick = onAlbumClick,
        currentPlayingSong = audioState.currentMediaId,
        modifier = modifier,
      )
    }

    HomeUiState.Empty -> {
      //EmptyScreen()
    }
  }
}

@Composable
internal fun HomeScreen(
  uiState: HomeUiState.Success,
  sortParams: SortParams,
  onSongClick: (Int) -> Unit,
  onArtistClick: (Long) -> Unit,
  onAlbumClick: (Long) -> Unit,
  currentPlayingSong: String,
  modifier: Modifier = Modifier,
) {

  Scaffold(
    topBar = {
      TroonaTopBar(hasLogo = true, searchWidgetState = { /*TODO*/ })
    }
  ) { innerPadding ->
    MediaPager(
      songs = uiState.songs,
      artists = uiState.artists,
      albums = uiState.albums,
      sortParams = sortParams,
      currentPlayingSongId = currentPlayingSong,
      onSongClick = onSongClick,
      onArtistClick = onArtistClick,
      onAlbumClick = onAlbumClick,
      modifier = modifier.padding(innerPadding)
    )
  }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
  HomeScreen(
    uiState = HomeUiState.Success(
      songs = emptyList(),
      artists = emptyList(),
      albums = emptyList(),
      songSortBy = SongSortBy.Title
    ),
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
    currentPlayingSong = "",
    onSongClick = {},
    onArtistClick = {},
    onAlbumClick = {},
  )
}
