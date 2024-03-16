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

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.donfreddy.troona.core.media.TroonaState
import com.donfreddy.troona.core.ui.MediaPager
import com.donfreddy.troona.feature.player.PlayerViewModel
import com.donfreddy.troona.feature.player.UIEvents
import com.donfreddy.troona.feature.player.UIState

@Composable
fun HomeRoute(
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
        onSongClick = {startIndex->
          Log.d("HomeRoute", "onSongClick: $startIndex")

          viewModel.onHomeUiEvents(HomeUiEvent.Play(uiState.songs, startIndex))
        },
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
  onSongClick: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  MediaPager(
    songs = uiState.songs,
    currentPlayingSongId = "",
    onSongClick = onSongClick,
    modifier = modifier,
  )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
  HomeScreen(
    uiState = HomeUiState.Success(
      songs = emptyList(),
      artists = emptyList(),
    ),
    onSongClick = {},
  )
}
