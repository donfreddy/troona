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

import android.util.Log
import androidx.compose.runtime.collectAsState
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
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  val audioState = audioServiceHandler.audioState

  private var duration by savedStateHandle.saveable { mutableLongStateOf(0L) }
  var progress by savedStateHandle.saveable { mutableFloatStateOf(0.5f) }
  private var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
  var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
  //var currentPlayingSong by savedStateHandle.saveable { mutableStateOf(Song.EXAMPLE) }
  var currentPlayingSong = audioServiceHandler.currentSong

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
            Log.d("PlayerViewModel", "CurrentPlaying: ${mediaState.mediaItemIndex}")
            currentPlayingSong = audioServiceHandler.queue[mediaState.mediaItemIndex]
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
      is UIEvents.SeekToPrevious -> audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToPrevious)
      is UIEvents.Backward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
      is UIEvents.Forward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
      is UIEvents.Repeat -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
      is UIEvents.PlayPause -> audioServiceHandler.onPlayerEvents(PlayerEvent.PlayPause)

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
      is UIEvents.SelectedAudioChange -> {
        currentPlayingSong = audioServiceHandler.queue[uiEvent.index]
      }
    }
  }

  private fun calculateProgressValue(currentPosition: Long) {
    progress = if (currentPosition > 0) ((currentPosition.toFloat() / duration.toFloat()) * 100) else 0f
  }
}

sealed class UIEvents {
  data object PlayPause : UIEvents()
  data class SeekTo(val position: Float) : UIEvents()
  data object SeekToNext : UIEvents()
  data object SeekToPrevious : UIEvents()
  data object Backward : UIEvents()
  data object Forward : UIEvents()
  data object Repeat : UIEvents()
  data class UpdateProgress(val newProgress: Float) : UIEvents()
  data class SelectedAudioChange(val index: Int) : UIEvents()
}

sealed class UIState {
  data object Initial : UIState()
  data object Ready : UIState()
}
