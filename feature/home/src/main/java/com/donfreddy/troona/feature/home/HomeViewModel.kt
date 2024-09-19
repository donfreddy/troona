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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.donfreddy.troona.core.domain.usecase.settings.sort.SetSongSortByUseCase
import com.donfreddy.troona.core.domain.usecase.songs.GetSongsUseCase
import com.donfreddy.troona.core.media.AudioServiceConnection
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.model.enums.SongSortBy
import com.donfreddy.troona.core.model.enums.SortOrder
import com.donfreddy.troona.core.ui.controllers.SnackBarAction
import com.donfreddy.troona.core.ui.controllers.SnackBarController
import com.donfreddy.troona.core.ui.controllers.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class HomeViewModel @Inject constructor(
  private val audioServiceConnection: AudioServiceConnection,
  getSongsUseCase: GetSongsUseCase,
  //private val setSortOrderUseCase: SetSortOrderUseCase,
  private val setSongSortByUseCase: SetSongSortByUseCase,
) : ViewModel() {
  val audioState = audioServiceConnection.audioState

  private val songs = getSongsUseCase().stateIn(
    scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
  )

  val uiState: StateFlow<HomeUiState> = combine(songs, getSongsUseCase()) { songs, artists ->
    HomeUiState.Success(songs = songs, artists = artists)
  }.stateIn(
    scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = HomeUiState.Loading
  )

  fun play(songs: List<Song>, startIndex: Int = 0) {
    showSnackBar("Playing ${songs[startIndex].title} from ${songs[startIndex].albumName}")
    audioServiceConnection.play(songs, startIndex)
  }

  private fun showSnackBar(
    message: String,
    actionText: String? = null,
    action: (suspend () -> Unit)? = null
  ) {
    viewModelScope.launch {
      val snackBarAction = action?.let {
        SnackBarAction(name = actionText ?: "Action", action = it)
      }
      SnackBarController.sendEvent(
        event = SnackBarEvent(
          message = message,
          action = snackBarAction
        )
      )
    }
  }
  //fun onChangeSortOrder(sortOrder: SortOrder) = viewModelScope.launch { setSortOrderUseCase(sortOrder) }

  fun onChangeSongSortBy(songSortBy: SongSortBy) =
    viewModelScope.launch { setSongSortByUseCase(songSortBy) }
}

sealed interface HomeUiState {
  data object Loading : HomeUiState

  data class Success(
    val songs: List<Song>, val artists: List<Song>,
  ) : HomeUiState

  data object Empty : HomeUiState
}

private const val TAG = "HomeViewModel"
