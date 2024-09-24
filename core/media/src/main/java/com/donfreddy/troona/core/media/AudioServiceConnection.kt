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

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.donfreddy.troona.core.common.network.Dispatcher
import com.donfreddy.troona.core.common.network.TroonaDispatchers.Main
import com.donfreddy.troona.core.domain.usecase.settings.playing_queue.GetPlayingQueueIdsUseCase
import com.donfreddy.troona.core.domain.usecase.settings.playing_queue.GetPlayingQueueIndexUseCase
import com.donfreddy.troona.core.domain.usecase.settings.playing_queue.GetPlayingQueuePositionUseCase
import com.donfreddy.troona.core.domain.usecase.settings.playing_queue.SetPlayingQueueIdsUseCase
import com.donfreddy.troona.core.domain.usecase.settings.playing_queue.SetPlayingQueueIndexUseCase
import com.donfreddy.troona.core.domain.usecase.settings.playing_queue.SetPlayingQueuePositionUseCase
import com.donfreddy.troona.core.domain.usecase.songs.GetSongsUseCase
import com.donfreddy.troona.core.media.ShuffleHelper.makeShuffleList
import com.donfreddy.troona.core.media.mapper.asMediaItems
import com.donfreddy.troona.core.model.data.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

/**
 * Class that manages the connection to a [MediaSessionService] instance, typically a
 * [AudioService] or one of its subclasses.
 */
@UnstableApi
@Singleton
class AudioServiceConnection @Inject constructor(
  @ApplicationContext context: Context,
  @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
  private val useCases: UseCaseContainer
) {
  private var controller: MediaController? = null
  private val scope = CoroutineScope(mainDispatcher + SupervisorJob())

  private val playerListener: PlayerListener = PlayerListener()
  val player: Player? get() = controller

  private var originalPlayingQueue = ArrayList<Song>()

  @JvmField
  var playingQueue = ArrayList<Song>()

  @JvmField
  var position = -1

  @JvmField
  var shuffleMode = 0

  private val isLastTrack: Boolean
    get() = getPosition() == playingQueue.size - 1

  var repeatMode = 0
    private set(value) {
      when (value) {
        Player.REPEAT_MODE_OFF, Player.REPEAT_MODE_ONE, Player.REPEAT_MODE_ALL -> {
          field = value
          // setRepeatMode(value)
          // handleInternalChange(REPEAT_MODE_CHANGED)
        }
      }
    }

  val songDurationMillis: Int
    get() = -1

  val songProgressMillis: Int
    get() = -1

  private var queuesRestored = false

  private fun getPosition() = position

  private fun setPosition(position: Int) {
    //this.position = position
  }

  val currentSong: Song
    get() = getSongAt(getPosition())

  private val _audioState: MutableStateFlow<AudioState> = MutableStateFlow(AudioState())
  val audioState: StateFlow<AudioState> = _audioState.asStateFlow()

  val currentPosition = flow {
    while (currentCoroutineContext().isActive) {
      emit(controller?.currentPosition ?: 0L)
      delay(1.milliseconds)
    }
  }

  init {
    scope.launch {
      val newController = MediaController.Builder(
        context, SessionToken(context, ComponentName(context, AudioService::class.java))
      ).setListener(ControllerListener()).buildAsync().await().apply {
        addListener(playerListener)
      }
      controller = newController
    }
  }

  fun addSong(position: Int, song: Song) {
    playingQueue.add(position, song)
    originalPlayingQueue.add(position, song)
    notifyChange(QUEUE_CHANGED)
  }

  fun addSong(song: Song) {
    playingQueue.add(song)
    originalPlayingQueue.add(song)
    notifyChange(QUEUE_CHANGED)
  }

  private fun addSongs(position: Int, songs: List<Song>) {
    Timber.tag(TAG).d("Position %s", position)
    playingQueue.addAll(position, songs)
    originalPlayingQueue.addAll(position, songs)
    notifyChange(QUEUE_CHANGED)
  }

  private fun addSongs(songs: List<Song>) {
    playingQueue.addAll(songs)
    originalPlayingQueue.addAll(songs)
    notifyChange(QUEUE_CHANGED)
  }

  fun clearQueue() {
    playingQueue.clear()
    originalPlayingQueue.clear()
    setPosition(-1)
    notifyChange(QUEUE_CHANGED)
  }

  fun updateDeletedPosition(deletedPosition: Int) {
    if (deletedPosition < position) {
      position--
    } else if (deletedPosition == position) {
      if (playingQueue.size > deletedPosition) {
        TODO("Set position to next song")
      } else {
        TODO("Set position to previous song")
      }
    }
  }

  private suspend fun restoreQueuesAndPositionIfNecessary() {
    if (!queuesRestored) {
      with(useCases) {
        val storedQueueIds = getPlayingQueueIdsUseCase.invoke().first()
        val storedQueueIndex = getPlayingQueueIndexUseCase.invoke().first()
        //val storedQueuePosition = getPlayingQueuePositionUseCase.invoke().first()

        val songs = getSongsUseCase.invoke().first()
        val playingSongs =
          songs.filter { song -> storedQueueIds.contains(song.id.toString()) }
        playingQueue = ArrayList(playingSongs)
        position = storedQueueIndex

        scope.launch {}
      }
      queuesRestored = true
    }
  }

  fun play() = controller?.play()

  fun pause() = controller?.pause()

  fun play(songs: List<Song>, startIndex: Int) {
    //setMediaItems(songs)
    addSongs(songs)
    controller?.run {
      prepare()
      when (startIndex) {
        currentMediaItemIndex -> {
          Timber.tag(TAG).d("Playing current song")
          if (isPlaying) {
            pause()
            seekToDefaultPosition()
            play()
          } else {
            play()
          }
        }

        else -> {
          Timber.tag(TAG).d("Seeking to $startIndex")
          Timber.tag(TAG).d("Current index in queue: $currentMediaItemIndex")
          //Timber.tag(TAG).d("Current index in playing queue: $position")
          //seekTo(startIndex, C.TIME_UNSET)
          position = startIndex
          seekToDefaultPosition(getPosition())
          _audioState.update { it.copy(playWhenReady = true) }
          playWhenReady = true
        }
      }
    }
  }

  private fun playSongAt(index: Int) {
    this.position = index
    controller?.run {
      seekToDefaultPosition(index)
      position = index
      playWhenReady = true
      _audioState.update { it.copy(playWhenReady = true) }
    }
    //notifyChange(META_CHANGED)
  }

  fun seekToPrevious() = controller?.run {
    seekToPrevious()
    play()
  }

  fun seekToNext() = controller?.run {
    seekToNext()
    play()
  }

  fun seekTo(position: Long) = controller?.run {
    seekTo(position)
    play()
  }

  fun skipToIndex(index: Int, position: Long = C.TIME_UNSET) = controller?.run {
    seekTo(index, position)
    play()
  }

  fun stop() {
    controller?.stop()
    notifyChange(PLAY_STATE_CHANGED)
  }

  fun openQueue(
    playingQueue: List<Song>?,
    startPosition: Int,
    startPlaying: Boolean,
  ) {
    if (!playingQueue.isNullOrEmpty()
      && startPosition >= 0 && startPosition < playingQueue.size
    ) {
      // it is important to copy the playing queue here first as we might add/remove songs later
      originalPlayingQueue = ArrayList(playingQueue)
      this.playingQueue = ArrayList(originalPlayingQueue)
      var position = startPosition
      if (shuffleMode == SHUFFLE_MODE_ALL) {
        makeShuffleList(this.playingQueue, startPosition)
        position = 0
      }
      if (startPlaying) {
        playSongAt(position)
      } else {
        setPosition(position)
      }
      notifyChange(QUEUE_CHANGED)
    }
  }

  fun release() {
    controller?.let {
      it.removeListener(playerListener)
      it.release()
    }
    instance = null
  }

  /**
   * Listener for the [MediaController] to update the UI when the player state changes.
   * We use a [Player.Listener] to listen to the player events.
   */
  private inner class PlayerListener : Player.Listener {
    override fun onEvents(player: Player, events: Player.Events) {
      /** Save the current media item position */
      if (events.contains(Player.EVENT_POSITION_DISCONTINUITY) || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION) || events.contains(
          Player.EVENT_PLAY_WHEN_READY_CHANGED
        )
      ) {
        savePlayingQueuePosition(player)
        savePlayingQueue()
      }

      /** Update the current media item */
      if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
        updatePlayingQueueIndex(player)
      }

      /** Update audio state */
      if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED) || events.contains(
          Player.EVENT_MEDIA_METADATA_CHANGED
        )
      ) {
        updateAudioState(player)
      }
    }
  }

  private inner class ControllerListener : MediaController.Listener {
    override fun onDisconnected(controller: MediaController) {
      release()
    }
  }

  private fun updateAudioState(player: Player) {
    with(player) {
      val state = AudioState(
        currentMediaId = currentMediaItem?.mediaId ?: "",
        currentMediaIndex = currentMediaItemIndex,
        playbackState = playbackState,
        playWhenReady = playWhenReady,
        duration = currentPosition
      )
      _audioState.update { state }
    }
  }

  private fun updatePlayingQueueIndex(player: Player) {
    val index = player.currentMediaItemIndex
    if (index == -1) return
    _audioState.update { it.copy(currentMediaIndex = index) }
    with(useCases) {
      scope.launch { setPlayingQueueIndexUseCase(index) }
    }
  }

  private fun savePlayingQueuePosition(player: Player) {
    val position = player.currentPosition
    _audioState.update { it.copy(duration = position) }
    with(useCases) {
      scope.launch { setPlayingQueuePositionUseCase(position) }
    }
  }

  private fun savePlayingQueue() {
    with(useCases) {
      scope.launch {
        setPlayingQueueIdsUseCase(playingQueue.map { it.id.toString() })
        setPlayingQueueIndexUseCase(position)
      }
    }
  }

  private fun getSongAt(position: Int): Song {
    return if ((position >= 0) && (position < playingQueue.size)) {
      playingQueue[position]
    } else {
      Song.EXAMPLE
    }
  }

  fun openQueue(
    songs: List<Song>, startPosition: Int, startPositionMs: Long, playWhenReady: Boolean
  ) {
    if (playingQueue.isNotEmpty() && startPosition >= 0 && startPosition < playingQueue.size) {
      Log.d(TAG, "Queue already opened")
      // notifyChange(PLAYING_QUEUE_OPENED)
    }
  }

  private fun notifyChange(what: String) {
    handleInternalChange(what)
    // sendPublicIntent(what)
  }

  /**
   * Handle internal changes in the service.
   * @param what The change that occurred.
   */
  private fun handleInternalChange(what: String) {
    when (what) {
      PLAY_STATE_CHANGED -> {
        val isPlaying = audioState.value.isPlaying
        //if (!isPlaying && songProgressMillis > 0) {
        //
        // TODO(Don): Save position in track
        //savePositionInTrack()
        // savePlayingQueuePosition()
        //}
        controller?.run {
          if (audioState.value.isPlaying) {
            play()
          } else {
            pause()
          }
        }
      }

      QUEUE_CHANGED -> {
        controller?.setMediaItems(playingQueue.asMediaItems())
        savePlayingQueue()
        if (playingQueue.size > 0) {
          Log.d(TAG, "Queue changed")
        } else {
          Log.d(TAG, "Queue is empty")
        }
      }
    }
  }

  private fun getNextPosition(force: Boolean): Int {
    var position = getPosition() + 1
    when (repeatMode) {
      REPEAT_MODE_NONE -> if (isLastTrack) {
        position -= 1
      }

      REPEAT_MODE_ALL -> if (isLastTrack) {
        position = 0
      }

      REPEAT_MODE_ONE -> if (force) {
        if (isLastTrack) {
          position = 0
        }
      } else {
        position -= 1
      }
    }
    return position
  }

  private fun getPreviousPosition(force: Boolean): Int {
    var newPosition = getPosition() - 1
    when (repeatMode) {
      REPEAT_MODE_NONE -> if (newPosition < 0) {
        newPosition = 0
      }

      REPEAT_MODE_ONE -> if (force) {
        if (newPosition < 0) {
          newPosition = playingQueue.size - 1
        }
      } else {
        newPosition = getPosition()
      }

      REPEAT_MODE_ALL -> if (newPosition < 0) {
        newPosition = playingQueue.size - 1
      }
    }
    return newPosition
  }

  fun restoreState(completion: () -> Unit = {}) {
    scope.launch {
      restoreQueuesAndPositionIfNecessary()
      completion()
    }
  }

  /**
   * Companion object to hold constants and static methods.
   */

  companion object {
    @Volatile
    private var instance: AudioServiceConnection? = null

    fun getInstance(
      context: Context, mainDispatcher: CoroutineDispatcher, useCases: UseCaseContainer
    ) = instance ?: synchronized(this) {
      instance ?: AudioServiceConnection(
        context, mainDispatcher, useCases
      ).also { instance = it }
    }

    /** Constants */
    val TAG: String = AudioServiceConnection::class.java.simpleName
    private const val TROONA_PACKAGE_NAME = "com.donfreddy.troona"
    const val MUSIC_PACKAGE_NAME = "com.android.music"
    const val META_CHANGED = "$TROONA_PACKAGE_NAME.meta_changed"
    const val QUEUE_CHANGED = "$TROONA_PACKAGE_NAME.queue_changed"
    const val PLAY_STATE_CHANGED = "$TROONA_PACKAGE_NAME.play_state_changed"
    const val REPEAT_MODE_CHANGED = "$TROONA_PACKAGE_NAME.repeat_mode_changed"
    const val SHUFFLE_MODE_CHANGED = "$TROONA_PACKAGE_NAME.shuffle_mode_changed"
    const val FAVORITE_STATE_CHANGED = "$TROONA_PACKAGE_NAME.favorite_state_changed"
    const val SHUFFLE_MODE_NONE = 0
    const val SHUFFLE_MODE_ALL = 1
    const val REPEAT_MODE_NONE = 0
    const val REPEAT_MODE_ALL = 1
    const val REPEAT_MODE_ONE = 2
  }
}

object ShuffleHelper {

  fun makeShuffleList(listToShuffle: MutableList<Song>, current: Int) {
    if (listToShuffle.isEmpty()) return
    if (current >= 0) {
      val song = listToShuffle.removeAt(current)
      listToShuffle.shuffle()
      listToShuffle.add(0, song)
    } else {
      listToShuffle.shuffle()
    }
  }
}

data class AudioState(
  val currentMediaId: String = "",
  val currentMediaIndex: Int = C.INDEX_UNSET,
  val playbackState: Int = Player.STATE_IDLE,
  val playWhenReady: Boolean = false,
  val duration: Long = C.TIME_UNSET
) {
  val isPlaying: Boolean
    get() {
      return (playbackState == Player.STATE_BUFFERING || playbackState == Player.STATE_READY) && playWhenReady
    }
}

/** UseCase use in AudioServiceConnection */
data class UseCaseContainer @Inject constructor(
  val getSongsUseCase: GetSongsUseCase,
  val getPlayingQueueIdsUseCase: GetPlayingQueueIdsUseCase,
  val setPlayingQueueIdsUseCase: SetPlayingQueueIdsUseCase,
  val getPlayingQueueIndexUseCase: GetPlayingQueueIndexUseCase,
  val setPlayingQueueIndexUseCase: SetPlayingQueueIndexUseCase,
  val getPlayingQueuePositionUseCase: GetPlayingQueuePositionUseCase,
  val setPlayingQueuePositionUseCase: SetPlayingQueuePositionUseCase
)
