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

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.donfreddy.troona.core.domain.usecase.songs.GetSongsUseCase
import com.donfreddy.troona.core.media.PlayerEvent
import com.donfreddy.troona.core.media.TroonaServiceHandler
import com.donfreddy.troona.core.model.data.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
  private val audioServiceHandler: TroonaServiceHandler,
  getSongsUseCase: GetSongsUseCase,
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  val audioState = audioServiceHandler.audioState

  var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
  var currentPlayingSong by savedStateHandle.saveable { mutableStateOf(Song.EXAMPLE) }
  val currentMediaItemIndex = audioServiceHandler.currentMediaItemIndex

  private val songs = getSongsUseCase().stateIn(
    scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
  )

  val uiState: StateFlow<HomeUiState> = combine(songs, getSongsUseCase()) { songs, artists ->
    HomeUiState.Success(songs = songs, artists = artists)
  }.stateIn(
    scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = HomeUiState.Loading
  )

  fun onHomeUiEvents(uiEvent: HomeUiEvent) = viewModelScope.launch {
    when (uiEvent) {
      is HomeUiEvent.Play -> {
        audioServiceHandler.onPlayerEvents(
          PlayerEvent.Play(songs = uiEvent.songs),
          startIndex = uiEvent.startIndex
        )
      }

      is HomeUiEvent.SelectedSongChange -> {
        Log.d("HomeRoute", "onSongClick: ${uiEvent.index}")
        Log.d("HomeRoute", "Queue: ${audioServiceHandler.queue}")
        currentPlayingSong = audioServiceHandler.queue[uiEvent.index]
      }
    }
  }
}

sealed class HomeUiEvent {
  data class Play(val songs: List<Song>, val startIndex: Int = 0) : HomeUiEvent()
  data class SelectedSongChange(val index: Int) : HomeUiEvent()
}

sealed interface HomeUiState {
  data object Loading : HomeUiState

  data class Success(
    val songs: List<Song>,
    val artists: List<Song>
  ) : HomeUiState

  data object Empty : HomeUiState
}