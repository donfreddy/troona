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

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.donfreddy.troona.core.common.network.Dispatcher
import com.donfreddy.troona.core.common.network.TroonaDispatchers.Main
import com.donfreddy.troona.core.media.notification.TroonaNotificationProvider
import com.donfreddy.troona.core.media.util.unsafeLazy
import com.donfreddy.troona.core.model.data.Song
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class AudioService : MediaSessionService() {
  @Inject
  lateinit var mediaSession: MediaSession

  @Inject
  lateinit var notificationProvider: TroonaNotificationProvider

  @Inject
  @Dispatcher(Main)
  lateinit var mainDispatcher: CoroutineDispatcher
  private val serviceScope by unsafeLazy { CoroutineScope(mainDispatcher + SupervisorJob()) }

  private var queuesRestored = false
  private var currentMediaItemIndex = 0
  private val playerListener = PlayerEventListener()

  @JvmField
  var shuffleMode = 0

  override fun onCreate() {
    Log.d(TAG, "AudioService created")
    super.onCreate()
    //setMediaNotificationProvider(notificationProvider)
  }

  @UnstableApi
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (intent != null && intent.action != null) {
      Log.d(TAG, "Received intent with action: ${intent.action}")
      serviceScope.launch {
        // TODO: restore the queue with the current media item if needed

        /** Handle the intent action here */
        when (intent.action) {
          ACTION_PLAY -> {
            Log.d(TAG, "Play action")
          }
        }
      }
    }
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onTaskRemoved(rootIntent: Intent?) {
    Log.d(TAG, "Task removed")
    super.onTaskRemoved(rootIntent)
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession =
    mediaSession

  override fun onDestroy() {
    Log.d(TAG, "AudioService destroyed")
    super.onDestroy()
    releaseMediaSession()
  }

  @UnstableApi
  override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
    super.onUpdateNotification(session, startInForegroundRequired)
  }

  init {
    Log.d(TAG, "AudioService created")
  }

  /**
   * Release the [MediaSession] and [Player] instances.
   * This method is called when the service is going away.
   * It is also called when the service is stopped due to an error.
   */
  private fun releaseMediaSession() {
    mediaSession.run {
      release()
      if (player.playbackState != Player.STATE_IDLE) {
        player.removeListener(playerListener)
        player.release()
      }
    }
    serviceScope.cancel()
  }

  /**
   * Send a public intent to notify other apps of the change.
   * e.g. musixmatch, last.fm, etc.
   * @param what The change that occurred.
   */
  fun sendPublicIntent(what: String) {
    val intent = Intent(what.replace(TROONA_PACKAGE_NAME, "com.android.music."))
    val song = Song.EXAMPLE
    intent.putExtra("id", song.id)
    intent.putExtra("artist", song.artistName)
    intent.putExtra("album", song.albumName)
    intent.putExtra("track", song.title)
    intent.putExtra("duration", song.duration)
    intent.putExtra("position", 0L)
    intent.putExtra("playing", false)
    intent.putExtra("scrabbling_source", TROONA_PACKAGE_NAME)
    @Suppress("Deprecation")
    sendStickyBroadcast(intent)
  }

  /** Listener for player events from [Player]. */
  private inner class PlayerEventListener : Player.Listener {
    override fun onEvents(player: Player, events: Player.Events) {
      if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)
        || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
        || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
      ) {
        currentMediaItemIndex = player.currentMediaItemIndex
        // TODO(freddy): save the current media item here
      }
    }

    override fun onPlayerError(error: PlaybackException) {
      var message = R.string.core_media_generic_error;
      Log.e(
        TAG,
        "Player error: " + error.errorCodeName + " (" + error.errorCode + ")",
        error
      );
      if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
        || error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND
      ) {
        message = R.string.core_media_error_media_not_found
      }
      Toast.makeText(
        applicationContext,
        message,
        Toast.LENGTH_LONG
      ).show()
    }
  }

  companion object {
    val TAG: String = AudioService::class.java.simpleName
    private const val TROONA_PACKAGE_NAME = "com.donfreddy.troona"
    const val ACTION_PLAY = "$TROONA_PACKAGE_NAME.play"
  }
}