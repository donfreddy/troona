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

package com.donfreddy.troona.core.media.di

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.donfreddy.troona.core.common.util.SDKVersionUtil
import com.donfreddy.troona.core.media.TroonaServiceHandler
import com.donfreddy.troona.core.media.notification.TroonaNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {
  @Provides
  @Singleton
  fun provideAudioAttributes(): AudioAttributes =
    AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA)
      .build()

  @Provides
  @Singleton
  @UnstableApi
  fun provideExoPlayer(
    @ApplicationContext context: Context, audioAttributes: AudioAttributes
  ): ExoPlayer = ExoPlayer.Builder(context).setAudioAttributes(audioAttributes, true)
    .setHandleAudioBecomingNoisy(true).setTrackSelector(DefaultTrackSelector(context)).build()

  @Provides
  @Singleton
  fun provideSessionActivityPendingIntent(@ApplicationContext context: Context): PendingIntent {
    val imFlag = if (SDKVersionUtil.isMarshmallowOrHigher) PendingIntent.FLAG_IMMUTABLE else 0
    return TaskStackBuilder.create(context).run {
      addNextIntent(Intent(context, Class.forName("com.donfreddy.troona.ui.MainActivity")))
      getPendingIntent(0, imFlag or PendingIntent.FLAG_UPDATE_CURRENT)
    }
  }

  @Provides
  @Singleton
  fun provideMediaSession(
    @ApplicationContext context: Context,
    player: ExoPlayer,
    sessionActivityPendingIntent: PendingIntent
  ): MediaSession = MediaSession.Builder(context, player)
    .setSessionActivity(sessionActivityPendingIntent).build()

  @Provides
  @Singleton
  fun provideNotificationManager(
    @ApplicationContext context: Context,
    player: ExoPlayer
  ): TroonaNotificationManager =
    TroonaNotificationManager(context, player)

  @Provides
  @Singleton
  fun provideServiceHelper(player: ExoPlayer): TroonaServiceHandler = TroonaServiceHandler(player)
}
