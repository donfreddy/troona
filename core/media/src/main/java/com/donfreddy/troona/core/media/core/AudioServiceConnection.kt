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

package com.donfreddy.troona.core.media.core

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.donfreddy.troona.core.common.network.Dispatcher
import com.donfreddy.troona.core.common.network.TroonaDispatchers.Main
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Class that manages the connection to a [MediaSessionService] instance, typically a
 * [AudioService] or one of its subclasses.
 */
@Singleton
class AudioServiceConnection @Inject constructor(
  @ApplicationContext context: Context,
  @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
) : Playback {
  private var controller: MediaController? = null
  private val scope = CoroutineScope(mainDispatcher + SupervisorJob())
  private val playerListener: PlayerListener = PlayerListener()
  val player: Player? get() = controller

  @JvmField
  var playingQueue = MutableLiveData<List<MediaItem>>()
    .apply { postValue(emptyList()) }

  @JvmField
  val nowPlaying = MutableLiveData<MediaItem>()
    .apply { postValue(MediaItem.EMPTY) }

  private val _audioState: MutableStateFlow<AudioState> = MutableStateFlow(AudioState())
  val audioState: StateFlow<AudioState> = _audioState.asStateFlow()

  init {
    scope.launch {
      val newController =
        MediaController.Builder(
          context,
          SessionToken(context, ComponentName(context, AudioService::class.java))
        )
          .setListener(ControllerListener())
          .buildAsync()
          .await().apply {
            addListener(PlayerListener())
          }
      controller = newController
    }
  }


  private fun updateNowPlaying(player: Player) {
    val mediaItem = player.currentMediaItem ?: MediaItem.EMPTY
    if (mediaItem == MediaItem.EMPTY) return
    scope.launch { nowPlaying.postValue(mediaItem) }
  }

  companion object {
    @Volatile
    private var instance: AudioServiceConnection? = null

    fun getInstance(context: Context, mainDispatcher: CoroutineDispatcher) =
      instance ?: synchronized(this) {
        instance ?: AudioServiceConnection(context, mainDispatcher)
          .also { instance = it }
      }
  }

  private inner class PlayerListener : Player.Listener {
    override fun onEvents(player: Player, events: Player.Events) {
      super.onEvents(player, events)
    }
  }

  private inner class ControllerListener : MediaController.Listener {
    override fun onDisconnected(controller: MediaController) {
      release()
    }
  }

  override fun play() {
    controller?.play()
  }

  override fun pause() {
    controller?.pause()
  }

  override fun stop() {
    controller?.stop()
  }

  override fun seekTo(position: Long) {
    controller?.seekTo(position)
  }

  override fun skipToPrevious() {
    controller?.run {
      seekToPrevious()
      play()
    }
  }

  override fun skipToNext() {
    controller?.run {
      seekToNext()
      play()
    }
  }

  override fun prepare() {
    controller?.prepare()
  }

  override fun release() {
    nowPlaying.postValue(MediaItem.EMPTY)
    controller?.let {
      it.removeListener(playerListener)
      it.release()
    }
    instance = null
  }
}

interface Playback {
  fun play()
  fun pause()
  fun stop()
  fun seekTo(position: Long)
  fun skipToPrevious()
  fun skipToNext()

  /*  fun setVolume(volume: Float)
    fun setPlaybackSpeed(speed: Float)
    fun setRepeatMode(repeatMode: Int)
    fun setShuffleModeEnabled(shuffleModeEnabled: Boolean)
    fun setMediaItem(mediaItem: MediaItem)
    fun setMediaItems(mediaItems: List<MediaItem>)*/
  fun prepare()
  fun release()
}

data class AudioState(
  val currentMediaId: String = "",
  val currentMediaIndex: Int = 0,
  val playbackState: Int = Player.STATE_IDLE,
  val playWhenReady: Boolean = false,
  val duration: Long = 0
)