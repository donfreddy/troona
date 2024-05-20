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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.donfreddy.troona.core.media.AudioServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
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

  fun onPlay() {
    audioServiceConnection.play()
  }

  fun onPause() {
    audioServiceConnection.pause()
  }

  fun onStop() {}

  fun onSkipPrevious() {
    audioServiceConnection.seekToPrevious()
  }

  fun onSkipNext() {
    audioServiceConnection.seekToNext()
  }

  fun onSkipTo(position: Long) {
    audioServiceConnection.seekTo(position)
  }

  fun onSkipToIndex(index: Int) {
    audioServiceConnection.skipToIndex(index)
  }

  fun onShuffle() {
    // Implement method
  }

  fun onRepeat() {
    // Implement method
  }

  fun setVolume(volume: Float) {
    // Implement method
  }

  fun moveQueueItem(currentIndex: Int, newIndex: Int) {
    // Implement method
  }

  fun removeQueueItemAt(index: Int) {
    // Implement method
  }

  fun skipToQueueItem(index: Int) {
    // Implement method
  }
}

sealed class UIState {
  data object Initial : UIState()
  data object Ready : UIState()
}
