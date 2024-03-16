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

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import coil.Coil
import coil.request.ImageRequest
import com.donfreddy.troona.core.common.network.Dispatcher
import com.donfreddy.troona.core.common.network.TroonaDispatchers.IO
import com.donfreddy.troona.core.common.network.TroonaDispatchers.Main
import com.donfreddy.troona.core.media.util.unsafeLazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@UnstableApi
class TroonaNotificationAdapter(
  private val context: Context,
  private val pendingIntent: PendingIntent?,
) : PlayerNotificationManager.MediaDescriptionAdapter {
  @Inject
  @Dispatcher(IO)
  lateinit var ioDispatcher: CoroutineDispatcher
  private val coroutineScope by unsafeLazy { CoroutineScope(ioDispatcher + SupervisorJob()) }

  @Inject
  @Dispatcher(Main)
  lateinit var mainDispatcher: CoroutineDispatcher
  private val mainScope by unsafeLazy { CoroutineScope(mainDispatcher + SupervisorJob()) }

  override fun getCurrentContentTitle(player: Player) = player.mediaMetadata.albumTitle ?: "Unknown"

  override fun createCurrentContentIntent(player: Player) = pendingIntent

  override fun getCurrentContentText(player: Player) =
    player.mediaMetadata.displayTitle ?: "Unknown"


  override fun getCurrentLargeIcon(
    player: Player, callback: PlayerNotificationManager.BitmapCallback
  ): Bitmap? {
    // Assuming you want to load an image from a URL stored in player.mediaMetadata.artworkUri
    val request = ImageRequest.Builder(context).data(player.mediaMetadata.artworkUri).build()

    coroutineScope.launch {
      val result = (Coil.imageLoader(context).execute(request).drawable as BitmapDrawable).bitmap
      withContext(mainScope.coroutineContext) {
        callback.onBitmap(result)
      }
    }
    return null
  }
}