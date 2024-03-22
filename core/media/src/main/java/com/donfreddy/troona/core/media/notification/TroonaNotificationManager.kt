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

package com.donfreddy.troona.core.media.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.donfreddy.troona.core.common.util.SDKVersionUtil
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.media.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class TroonaNotificationManager @Inject constructor(
  @ApplicationContext private val context: Context,
  private val exoPlayer: ExoPlayer,
) {
  private val notificationManager = NotificationManagerCompat.from(context)

  init {
    if (SDKVersionUtil.isOreoOrHigher) {
      createNotificationChannel()
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  @UnstableApi
  fun startNotificationService(
    mediaSessionService: MediaSessionService,
    mediaSession: MediaSession,
  ) {
    buildNotification(mediaSession = mediaSession)
    startForegroundService(mediaSessionService = mediaSessionService)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun startForegroundService(mediaSessionService: MediaSessionService) {
    val notification = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
      .setCategory(Notification.CATEGORY_SERVICE)
      .setSmallIcon(TroonaIcons.Music.resourceId)
      .build()
    mediaSessionService.startForeground(NOTIFICATION_ID, notification)
  }

  @UnstableApi
  private fun buildNotification(mediaSession: MediaSession) {
    PlayerNotificationManager.Builder(
      context,
      NOTIFICATION_ID,
      NOTIFICATION_CHANNEL_ID,
    ).setMediaDescriptionAdapter(
      TroonaNotificationAdapter(context, mediaSession.sessionActivity)
    ).setSmallIconResourceId(TroonaIcons.Music.resourceId)
      .build().also {
        //it.setMediaSessionToken(mediaSession.sessionCompatToken)
        it.setUseFastForwardActionInCompactView(true)
        it.setUseRewindActionInCompactView(true)
        it.setUseNextActionInCompactView(true)
        it.setPriority(NotificationCompat.PRIORITY_LOW)
        it.setPlayer(exoPlayer)
      }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createNotificationChannel() {
    val channel = NotificationChannel(
      NOTIFICATION_CHANNEL_ID,
      context.getString(R.string.core_media_playback_channel_name),
      NotificationManager.IMPORTANCE_LOW
    )
    notificationManager.createNotificationChannel(channel)
  }

  companion object {
    private const val NOTIFICATION_ID = 101
    private const val NOTIFICATION_CHANNEL_ID = "playback_channel"
  }
}