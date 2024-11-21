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

package com.donfreddy.troona.feature.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.donfreddy.troona.core.designsystem.component.TroonaTopBar
import com.donfreddy.troona.core.model.data.Album
import com.donfreddy.troona.core.model.data.Artist
import com.donfreddy.troona.core.model.enums.AlbumSortBy
import com.donfreddy.troona.core.model.enums.ArtistSortBy
import com.donfreddy.troona.core.model.enums.SongSortBy
import com.donfreddy.troona.core.model.enums.SortOrder
import com.donfreddy.troona.core.ui.component.MediaPager
import com.donfreddy.troona.core.ui.component.SortParams
import timber.log.Timber

@Composable
internal fun ArtistScreen(
  onBackClick: () -> Unit,
  viewModel: ArtistViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  //val audioState by viewModel.audioState.collectAsStateWithLifecycle()

  when (val uiState = state) {
    ArtistUiState.Loading -> Unit

    is ArtistUiState.Success -> {
      ArtistScreen(
        artist = uiState.artist,
        albums = uiState.albums,
        onBackClick = onBackClick,
        modifier = Modifier
      )
    }
  }
}

@Composable
private fun ArtistScreen(
  artist: Artist,
  albums: List<Album>,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
) {

  Scaffold(
    topBar = {
      TroonaTopBar(searchWidgetState = { /*TODO*/ }, onBackClick = onBackClick)
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(color = Color.DarkGray)
        .wrapContentSize(Alignment.Center)
        .padding(innerPadding)
    ) {
      Text(
        text = "Artist name: ${artist.name}",
        modifier = Modifier.align(Alignment.CenterHorizontally),
        textAlign = TextAlign.Center,
        color = Color.White
        //style = MaterialTheme.typography.h6
      )
      // album count
      Text(
        text = "Albums: ${albums.size}",
        modifier = Modifier.align(Alignment.CenterHorizontally),
        textAlign = TextAlign.Center,
        color = Color.White
        //style = MaterialTheme.typography.h6
      )
    }
  }
}

@Preview(
  showBackground = true,
  name = "Favorites screen"
)
@Composable
fun FavoritesScreenPreview() {
  ArtistScreen(
    artist = Artist.empty,
    albums = listOf(
      Album.empty,
      Album.empty,
      Album.empty
    ),
    onBackClick = {}
  )
}