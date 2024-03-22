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

import android.content.Context
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import com.donfreddy.troona.core.media.common.MediaConstants
import com.donfreddy.troona.core.media.mapper.asMediaItem
import com.donfreddy.troona.core.model.data.Song
import dagger.hilt.android.qualifiers.ApplicationContext
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
  private val exoPlayer: ExoPlayer,
) : Player.Listener {
  @Inject
  @ApplicationContext
  lateinit var context: Context

  private val _audioState: MutableStateFlow<TroonaState> = MutableStateFlow(TroonaState.Initial)
  val audioState: StateFlow<TroonaState> = _audioState.asStateFlow()

  private var job: Job? = null
  private val queueHandler = QueueHandler()

  @JvmField
  var playingQueue = ArrayList<Song>()

  fun setSong(song: Song) {
    exoPlayer.setMediaItem(song.asMediaItem())
  }

  // set volume
  fun setVolume(value: Float) {
    exoPlayer.volume = value
  }

  val queue get() = queueHandler.getQueue()
  val currentSong get() = if (queue.isNotEmpty()) queue[queueHandler.getCurrentIndex()] else Song.EXAMPLE
  val currentMediaItemIndex = queueHandler.getCurrentIndex()

  fun setSongs(songs: List<Song>) {
    exoPlayer.setMediaItems(songs.map(Song::asMediaItem))
    queueHandler.addQueueItems(songs)
    exoPlayer.prepare()
  }

  /**
   * This function is used to handle the player events
   * @param playerEvent The event to be handled
   * @param startIndex The index of the song to start playing
   * @param seekPosition The position to seek to in the song
   */
  suspend fun onPlayerEvents(
    playerEvent: PlayerEvent,
    startIndex: Int = -1,
    seekPosition: Long = 0
  ) {
    when (playerEvent) {
      is PlayerEvent.PlayPause -> playOrPause()

      is PlayerEvent.Play -> {
        setMediaItems(playerEvent.songs)
        queueHandler.addQueueItems(playerEvent.songs)
        // originalPlayingQueue = ArrayList(playerEvent.songs)
        playingQueue.clear()
        playingQueue.addAll(playerEvent.songs)

        exoPlayer.prepare()

        when (startIndex) {
          exoPlayer.currentMediaItemIndex -> {
            playOrPause()
          }

          else -> {
            exoPlayer.seekToDefaultPosition(startIndex)
            startPlayback()
          }
        }
      }

      is PlayerEvent.Backward -> exoPlayer.seekBack()

      is PlayerEvent.Forward -> exoPlayer.seekForward()

      is PlayerEvent.SeekToNext -> {
        if (exoPlayer.isPlaying) {
          exoPlayer.seekToNext()
        } else {
          exoPlayer.seekToNext()
          startPlayback()
        }
      }

      is PlayerEvent.SeekToPrevious -> exoPlayer.seekToPrevious()

      is PlayerEvent.SeekTo -> exoPlayer.seekTo(seekPosition)

      is PlayerEvent.Stop -> stopProgressUpdate()

      is PlayerEvent.UpdateProgress -> {
        exoPlayer.seekTo((exoPlayer.duration * playerEvent.newProgress).toLong())
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
      GlobalScope.launch { startProgressUpdate() }
    } else {
      stopProgressUpdate()
    }
  }

  override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    super.onShuffleModeEnabledChanged(shuffleModeEnabled)
  }

  override fun onRepeatModeChanged(repeatMode: Int) {
    super.onRepeatModeChanged(repeatMode)
  }

  override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
    super.onMediaItemTransition(mediaItem, reason)
    Toast.makeText(context, "Now playing: " + mediaItem?.mediaMetadata?.title, Toast.LENGTH_SHORT)
      .show();
    queueHandler.setCurrentIndex(exoPlayer.currentMediaItemIndex)
  }

  override fun onTimelineChanged(timeline: Timeline, @Player.TimelineChangeReason reason: Int) {
    if (reason == Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED) {
      // Update the UI according to the modified playlist (add, move or remove).
      // updateUiForPlaylist(timeline)
      TODO()
    }
  }

  private suspend fun playOrPause() {
    if (exoPlayer.isPlaying) {
      exoPlayer.pause()
      stopProgressUpdate()
    } else {
      startPlayback()
    }
  }

  private suspend fun startProgressUpdate() = job.run {
    while (true) {
      delay(MediaConstants.PROGRESS_UPDATE_INTERVAL)
      _audioState.value = TroonaState.Progress(exoPlayer.currentPosition)
    }
  }

  private fun stopProgressUpdate() {
    job?.cancel()
    _audioState.value = TroonaState.Playing(isPlaying = false)
  }

  private suspend fun startPlayback() {
    exoPlayer.playWhenReady = true
    _audioState.value = TroonaState.Playing(isPlaying = true)
    startProgressUpdate()
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
  data class Play(val songs: List<Song>) : PlayerEvent()
  data object PlayPause : PlayerEvent()
  data object Backward : PlayerEvent()
  data object Forward : PlayerEvent()
  data object SeekToNext : PlayerEvent()
  data object SeekToPrevious : PlayerEvent()
  data object SeekTo : PlayerEvent()
  data object Stop : PlayerEvent()
  data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}

sealed class TroonaState {
  data object Initial : TroonaState()
  data class Ready(val duration: Long) : TroonaState()
  data class Progress(val progress: Long) : TroonaState()
  data class Buffering(val progress: Long) : TroonaState()
  data class Playing(val isPlaying: Boolean) : TroonaState()
  data class CurrentPlaying(val mediaItemIndex: Int) : TroonaState()
}