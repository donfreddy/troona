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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.donfreddy.troona.core.domain.usecase.artists.GetArtistByIdUseCase
import com.donfreddy.troona.core.domain.usecase.settings.GetUserDataUseCase
import com.donfreddy.troona.core.media.AudioServiceConnection
import com.donfreddy.troona.core.model.data.Artist
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.model.enums.ArtistSortBy
import com.donfreddy.troona.feature.artist.navigation.getArtistId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class ArtistViewModel @Inject constructor(
  private val audioServiceConnection: AudioServiceConnection,
  getArtistByIdUseCase: GetArtistByIdUseCase,
  getUserDataUseCase: GetUserDataUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {
  val audioState = audioServiceConnection.audioState

  val uiState: StateFlow<ArtistUiState> =
    combine(
      getArtistByIdUseCase(savedStateHandle.getArtistId()),
      getUserDataUseCase()
    ) { artist, userData ->
      ArtistUiState.Success(
        artist = artist,
        artistSortBy = userData.artistSortBy
      )
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.Eagerly,
      initialValue = ArtistUiState.Loading
    )

  fun play(songs: List<Song>, startIndex: Int = 0) {
    audioServiceConnection.play(songs, startIndex)
  }

}

sealed interface ArtistUiState {
  data object Loading : ArtistUiState

  data class Success(
    val artist: Artist,
    val artistSortBy: ArtistSortBy
  ) : ArtistUiState
}