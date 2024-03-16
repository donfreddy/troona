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

package com.donfreddy.troona.feature.player

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.donfreddy.troona.core.media.PlayerEvent
import com.donfreddy.troona.core.media.TroonaServiceHandler
import com.donfreddy.troona.core.media.TroonaState
import com.donfreddy.troona.core.model.data.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class PlayerViewModel @Inject constructor(
  private val audioServiceHandler: TroonaServiceHandler,
  savableStateHandle: SavedStateHandle
) : ViewModel() {
  val audioState = audioServiceHandler.audioState

  private var duration by savableStateHandle.saveable { mutableLongStateOf(0L) }
  private var progress by savableStateHandle.saveable { mutableFloatStateOf(0f) }
  private var progressString by savableStateHandle.saveable { mutableStateOf("00:00") }
  private var isPlaying by savableStateHandle.saveable { mutableStateOf(false) }
  private var currentSelectedAudio by savableStateHandle.saveable { mutableStateOf(Song.EMPTY) }
  private var audioList by savableStateHandle.saveable { mutableStateOf(listOf<Song>()) }

  private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
  val uiState: StateFlow<UIState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      audioServiceHandler.audioState.collectLatest { mediaState ->
        when (mediaState) {
          TroonaState.Initial -> _uiState.value = UIState.Initial

          is TroonaState.Buffering -> calculateProgressValue(mediaState.progress)
          is TroonaState.Playing -> isPlaying = mediaState.isPlaying
          is TroonaState.Progress -> calculateProgressValue(mediaState.progress)
          is TroonaState.CurrentPlaying -> {
            currentSelectedAudio = audioList[mediaState.mediaItemIndex]
          }

          is TroonaState.Ready -> {
            duration = mediaState.duration
            _uiState.value = UIState.Ready
          }
        }
      }
    }
  }

  fun onUiEvent(uiEvent: UIEvents) = viewModelScope.launch {
    when (uiEvent) {
      is UIEvents.SeekToNext -> audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
      is UIEvents.Backward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
      is UIEvents.Forward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
      is UIEvents.Repeat -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
      is UIEvents.PlayPause -> audioServiceHandler.onPlayerEvents(PlayerEvent.PlayPause)
      //is UIEvents.SelectedAudioChange -> audioServiceHandler.onPlayerEvents(
        //PlayerEvent.SelectedAudioChange,
       // selectedAudioIndex = uiEvent.index
       //)

      is UIEvents.SeekTo -> {
        audioServiceHandler.onPlayerEvents(
          PlayerEvent.SeekTo,
          seekPosition = ((uiEvent.position * duration) / 100f).toLong()
        )
      }

      is UIEvents.UpdateProgress -> {
        audioServiceHandler.onPlayerEvents(
          PlayerEvent.UpdateProgress(uiEvent.newProgress)
        )
      }
      is UIEvents.GetCurrentPlayingSongId -> {
        audioServiceHandler.onPlayerEvents(
          PlayerEvent.GetCurrentPlayingSongId(uiEvent.index)
        )
      }
    }
  }

  private fun calculateProgressValue(currentPosition: Long) {
    progress =
      if (currentPosition > 0) ((currentPosition.toFloat() / duration.toFloat()) * 100) else 0f
  }
}

sealed class UIEvents {
  data object PlayPause : UIEvents()
  //data class SelectedAudioChange(val index: Int) : UIEvents()
  data class SeekTo(val position: Float) : UIEvents()
  data object SeekToNext : UIEvents()
  data object Backward : UIEvents()
  data object Forward : UIEvents()
  data object Repeat : UIEvents()
  data class UpdateProgress(val newProgress: Float) : UIEvents()
  data class GetCurrentPlayingSongId(val index: Int) : UIEvents()
}

sealed class UIState {
  data object Initial : UIState()
  data object Ready : UIState()
}
