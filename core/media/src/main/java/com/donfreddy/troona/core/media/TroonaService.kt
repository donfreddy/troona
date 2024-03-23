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

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.donfreddy.troona.core.common.util.SDKVersionUtil
import com.donfreddy.troona.core.media.notification.TroonaNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(UnstableApi::class) @AndroidEntryPoint
class TroonaService : MediaSessionService() {
  @Inject
  lateinit var mediaSession: MediaSession

  @Inject
  lateinit var notificationManager: TroonaNotificationManager

  // check if the device is running on Android Oreo or higher
  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
  fun isOreoOrHigher(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
  }

  init {
   Log.d("TroonaService", "onStartCommand: ")
    if (SDKVersionUtil.isOreoOrHigher) {
      Log.d("TroonaService", "onCreate: startForegroundService")
      notificationManager.startNotificationService(
        mediaSessionService = this,
        mediaSession = mediaSession
      )
    }
  }



   @UnstableApi
  override fun onCreate() {
    super.onCreate()
     Log.d("TroonaService", "onStartCommand: ")
     if (SDKVersionUtil.isOreoOrHigher) {
       Log.d("TroonaService", "onCreate: startForegroundService")
       notificationManager.startNotificationService(
         mediaSessionService = this,
         mediaSession = mediaSession
       )
     }
  }


  @UnstableApi
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

    return super.onStartCommand(intent, flags, startId)
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

  override fun onDestroy() {
    super.onDestroy()
    mediaSession.apply {
      release()
      player.release()
      if (player.playbackState != Player.STATE_IDLE) {
        player.seekTo(0)
        player.playWhenReady = false
        player.stop()
      }
    }
  }
}

private const val TROONA_ACTIVITY_PACKAGE_NAME = "com.donfreddy.troona.ui.MainActivity"