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

package com.donfreddy.troona.core.media

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.donfreddy.troona.core.media.common.MediaConstants
import com.donfreddy.troona.core.media.mapper.asMediaItem
import com.donfreddy.troona.core.model.data.Song
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "TroonaServiceHandler"

class TroonaServiceHandler @Inject constructor(
  private val exoPlayer: ExoPlayer
) : Player.Listener {
  private val _audioState: MutableStateFlow<TroonaState> = MutableStateFlow(TroonaState.Initial)
  val audioState: StateFlow<TroonaState> = _audioState.asStateFlow()

  private var job: Job? = null

  fun setSong(song: Song) {
    exoPlayer.setMediaItem(song.asMediaItem())
    exoPlayer.prepare()
  }

  fun setSongs(songs: List<Song>) {
    exoPlayer.setMediaItems(songs.map(Song::asMediaItem))
    exoPlayer.prepare()
  }

  suspend fun onPlayerEvents(
    playerEvent: PlayerEvent, startIndex: Int = -1, seekPosition: Long = 0
  ) {
    Log.d(TAG, "onPlayerEvents: $playerEvent")

    when (playerEvent) {
      is PlayerEvent.PlayPause -> playOrPause()

      is PlayerEvent.Play -> {
        Log.d(TAG, "onPlayerEvents: $startIndex")

        // log the songs
        Log.d(TAG, "onPlayerEventsSongs: ${playerEvent.songs}")

        setMediaItems(playerEvent.songs)

        //Log.d(TAG, "onPlayerEventsMediaItems: $mediaItems")

        //exoPlayer.setMediaItems(mediaItems, startIndex, 0)
        Log.d(TAG, "onPlayerEvents: $startIndex")

        exoPlayer.prepare()

        Log.d(TAG, "ExoPlayer Prepared")
        exoPlayer.seekToDefaultPosition(0)
        _audioState.value = TroonaState.Playing(isPlaying = true)
        exoPlayer.playWhenReady = true

       /* when (startIndex) {
          exoPlayer.currentMediaItemIndex -> {
            playOrPause()
          }

          else -> {
            exoPlayer.seekToDefaultPosition(startIndex)
            _audioState.value = TroonaState.Playing(isPlaying = true)
            exoPlayer.playWhenReady = true
            startProgressTracker()
          }
        }*/
      }

      is PlayerEvent.Backward -> exoPlayer.seekBack()

      is PlayerEvent.Forward -> exoPlayer.seekForward()

      is PlayerEvent.SeekToNext -> exoPlayer.seekToNext()

      is PlayerEvent.SeekToPrevious -> exoPlayer.seekToPrevious()

      is PlayerEvent.SeekTo -> exoPlayer.seekTo(seekPosition)

      is PlayerEvent.Stop -> stopProgressTracker()

      is PlayerEvent.UpdateProgress -> {
        exoPlayer.seekTo((exoPlayer.duration * playerEvent.newProgress).toLong())
      }
      is PlayerEvent.GetCurrentPlayingSongId -> {
        //_audioState.value = TroonaState.CurrentPlaying(exoPlayer.currentMediaItemIndex)
      }
    }
  }

  override fun onPlaybackStateChanged(playbackState: Int) {
    when (playbackState) {
      Player.STATE_BUFFERING -> {
        _audioState.value = TroonaState.Buffering(exoPlayer.currentPosition)
      }

      Player.STATE_READY -> {
        _audioState.value = TroonaState.Ready(exoPlayer.duration)
      }

      Player.STATE_ENDED -> {
        _audioState.value = TroonaState.Playing(isPlaying = false)
      }

      Player.STATE_IDLE -> {
        _audioState.value = TroonaState.Initial
      }
    }
  }

  @OptIn(DelicateCoroutinesApi::class)
  override fun onIsPlayingChanged(isPlaying: Boolean) {
    _audioState.value = TroonaState.Playing(isPlaying)
    _audioState.value = TroonaState.CurrentPlaying(exoPlayer.currentMediaItemIndex)
    if (isPlaying) {
      GlobalScope.launch {
        startProgressTracker()
      }
    } else {
      stopProgressTracker()
    }
  }

  private suspend fun playOrPause() {
    Log.d(TAG, "playOrPause: ${exoPlayer.isPlaying}")
    if (exoPlayer.isPlaying) {
      exoPlayer.pause()
      stopProgressTracker()
    } else {
      exoPlayer.play()
      _audioState.value = TroonaState.Playing(isPlaying = true)
      startProgressTracker()
    }
  }

  private suspend fun startProgressTracker() = job.run {
    while (true) {
      delay(MediaConstants.PROGRESS_UPDATE_INTERVAL)
      _audioState.value = TroonaState.Progress(exoPlayer.currentPosition)
    }
  }

  private fun stopProgressTracker() {
    job?.cancel()
    _audioState.value = TroonaState.Playing(isPlaying = false)
  }

  private fun setMediaItems(audioList: List<Song>) {
    audioList.map { audio ->
      MediaItem.Builder()
        .setMediaId(String.format("%s", audio.id))
        .setUri(audio.uri)
        .setMediaMetadata(
          MediaMetadata.Builder()
            .setArtworkUri(audio.albumArt)
            .setAlbumArtist(audio.albumArtist)
            .setDisplayTitle(audio.title)
            .setSubtitle(audio.displayName)
            .setIsPlayable(true)
            .build()
        )
        .build()
    }.also {
      exoPlayer.setMediaItems(it)
    }
  }
}

sealed class PlayerEvent {
  data class Play(val songs: List<Song>, val startIndex: Int = 0) : PlayerEvent()
  data object PlayPause : PlayerEvent()
  data object Backward : PlayerEvent()
  data object Forward : PlayerEvent()
  data object SeekToNext : PlayerEvent()
  data object SeekToPrevious : PlayerEvent()
  // data class SeekTo(val position: Long) : PlayerEvent()
  data object SeekTo : PlayerEvent()
  data object Stop : PlayerEvent()
  data class UpdateProgress(val newProgress: Float) : PlayerEvent()
  data class GetCurrentPlayingSongId(val index: Int) : PlayerEvent()
}

sealed class TroonaState {
  data object Initial : TroonaState()
  data class Ready(val duration: Long) : TroonaState()
  data class Progress(val progress: Long) : TroonaState()
  data class Buffering(val progress: Long) : TroonaState()
  data class Playing(val isPlaying: Boolean) : TroonaState()
  data class CurrentPlaying(val mediaItemIndex: Int) : TroonaState()
}