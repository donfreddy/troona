package com.donfreddy.troona.core.media.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import coil.ImageLoader
import coil.request.ImageRequest
import com.donfreddy.troona.core.common.network.Dispatcher
import com.donfreddy.troona.core.common.network.TroonaDispatchers.IO
import com.donfreddy.troona.core.common.network.TroonaDispatchers.Main
import com.donfreddy.troona.core.common.util.SDKVersionUtil
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.media.R
import com.google.common.collect.ImmutableList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@UnstableApi
class TroonaNotificationProvider @Inject constructor(
  @ApplicationContext private val context: Context,
  @Dispatcher(Main) mainDispatcher: CoroutineDispatcher,
  @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : MediaNotification.Provider {
  private val notificationManager = checkNotNull(context.getSystemService<NotificationManager>())
  private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())

  @RequiresApi(Build.VERSION_CODES.O)
  override fun createNotification(
    mediaSession: MediaSession,
    customLayout: ImmutableList<CommandButton>,
    actionFactory: MediaNotification.ActionFactory,
    onNotificationChangedCallback: MediaNotification.Provider.Callback
  ): MediaNotification {
    ensureNotificationChannel()
    val player = mediaSession.player
    /*val pendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
      addNextIntentWithParentStack(Intent(Intent.ACTION_VIEW, "troona://full_player".toUri()))
      getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)!!
    }*/

    val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
      .setContentTitle(player.mediaMetadata.title)
      .setContentText(player.mediaMetadata.artist)
      .setSmallIcon(TroonaIcons.Music.resourceId)
      .setPriority(NotificationCompat.PRIORITY_LOW)
      .setStyle(MediaStyleNotificationHelper.MediaStyle(mediaSession)).setContentIntent(
        // Todo: Implement pending intent to open full player when notification is clicked
        mediaSession.sessionActivity
      ).addAction(
        actionFactory.createCustomActionFromCustomCommandButton(
          mediaSession, customLayout.first()
        )
      )


    // Favorite action (like button)
    /* builder.addAction(
       actionFactory.createCustomActionFromCustomCommandButton(
         mediaSession, customLayout.first()
       )
     )*/


    listOf(
      createMediaAction(
        mediaSession,
        actionFactory,
        TroonaIcons.Favorite.resourceId,
        "Add to favorites",
        33
      ),
      createMediaAction(
        mediaSession,
        actionFactory,
        TroonaIcons.SkipPrevious.resourceId,
        "Skip previous",
        Player.COMMAND_SEEK_TO_PREVIOUS
      ),
      createMediaAction(
        mediaSession,
        actionFactory,
        if (player.playWhenReady) TroonaIcons.Pause.resourceId else TroonaIcons.Play.resourceId,
        if (player.playWhenReady) "Pause" else "Play",
        Player.COMMAND_PLAY_PAUSE
      ),
      createMediaAction(
        mediaSession,
        actionFactory,
        TroonaIcons.FastForward.resourceId,
        "Skip next",
        Player.COMMAND_SEEK_TO_NEXT
      )
    ).forEach { builder.addAction(it) }

    setupArtwork(uri = player.mediaMetadata.artworkUri,
      setLargeIcon = builder::setLargeIcon,
      updateNotification = {
        val notification = MediaNotification(NOTIFICATION_ID, builder.build())
        onNotificationChangedCallback.onNotificationChanged(notification)
      })

    return MediaNotification(NOTIFICATION_ID, builder.build())
  }

  override fun handleCustomCommand(session: MediaSession, action: String, extras: Bundle) = false

  @RequiresApi(Build.VERSION_CODES.O)
  private fun ensureNotificationChannel() {
    if (SDKVersionUtil.isOreoOrHigher || notificationManager.getNotificationChannel(
        NOTIFICATION_CHANNEL_ID
      ) != null
    ) return

    val notificationChannel = NotificationChannel(
      NOTIFICATION_CHANNEL_ID,
      context.getString(R.string.core_media_playback_channel_name),
      NotificationManager.IMPORTANCE_LOW
    )
    notificationManager.createNotificationChannel(notificationChannel)
  }

  private fun createMediaAction(
    mediaSession: MediaSession,
    actionFactory: MediaNotification.ActionFactory,
    iconRes: Int,
    title: String,
    command: Int
  ) = actionFactory.createMediaAction(
    mediaSession, IconCompat.createWithResource(context, iconRes), title, command
  )

  private fun setupArtwork(
    uri: Uri?, setLargeIcon: (Bitmap?) -> Unit, updateNotification: () -> Unit
  ) = coroutineScope.launch {
    val bitmap = loadArtworkBitmap(uri)
    setLargeIcon(bitmap)
    updateNotification()
  }

  private suspend fun loadArtworkBitmap(uri: Uri?) =
    withContext(ioDispatcher) { uri?.asArtworkBitmap(context) }

  companion object {
    private const val NOTIFICATION_ID = 101
    private const val NOTIFICATION_CHANNEL_ID = "playback_channel"
  }
}

internal suspend fun Uri.asArtworkBitmap(context: Context): Bitmap? {
  val loader = ImageLoader(context)
  val request = ImageRequest.Builder(context).data(this).placeholder(TroonaIcons.Music.resourceId)
    .error(TroonaIcons.Music.resourceId).build()

  val drawable = loader.execute(request).drawable
  return drawable?.toBitmap()
}