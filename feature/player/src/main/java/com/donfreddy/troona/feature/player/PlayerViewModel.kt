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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import com.donfreddy.troona.core.media.AudioServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class PlayerViewModel @Inject constructor(
  private val audioServiceConnection: AudioServiceConnection,
) : ViewModel() {
  val audioState = audioServiceConnection.audioState

  val currentPosition = audioServiceConnection.currentPosition.stateIn(
    scope = viewModelScope,
    started = SharingStarted.Eagerly,
    initialValue = 0L
  )

  val playingQueue = audioServiceConnection.playingQueue

  fun onEvent(event: UIEvents) = viewModelScope.launch {
    when (event) {
      is UIEvents.SeekToPrevious -> {
        Timber.tag("PlayerViewModel").d("SeekToPrevious")
        audioServiceConnection.seekToPrevious()
      }

      is UIEvents.Play -> {
        Timber.tag("PlayerViewModel").d("Play")
        audioServiceConnection.play()
      }

      is UIEvents.Pause -> {
        Timber.tag("PlayerViewModel").d("Pause")
        audioServiceConnection.pause()
      }

      is UIEvents.SeekToNext -> {
        Timber.tag("PlayerViewModel").d("SeekToNext")
        audioServiceConnection.seekToNext()
      }

      is UIEvents.SeekTo -> {
        Timber.tag("PlayerViewModel").d("Repeat")
        audioServiceConnection.seekTo(event.position)
      }

      is UIEvents.SkipToIndex -> {
        Timber.tag("PlayerViewModel").d("SeekTo")
        audioServiceConnection.skipToIndex(event.index)
      }
    }
  }
}

sealed class UIEvents {
  data object SeekToPrevious : UIEvents()
  data object Play : UIEvents()
  data object Pause : UIEvents()
  data object SeekToNext : UIEvents()
  data class SeekTo(val position: Long) : UIEvents()
  data class SkipToIndex(val index: Int) : UIEvents()
}

sealed class UIState {
  data object Initial : UIState()
  data object Ready : UIState()
}
