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

package com.donfreddy.troona.feature.player

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import com.donfreddy.troona.core.designsystem.component.SingleLineText
import com.donfreddy.troona.core.designsystem.component.TroonaIconButton
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.designsystem.images.TroonaArtwork
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.media.AudioState
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.feature.player.components.SeekBar
import com.donfreddy.troona.feature.player.components.TroonaSlider
import com.donfreddy.troona.feature.player.util.convertToPosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@UnstableApi
@Composable
fun FullPlayer(
  isPlayerOpened: Boolean,
  onSetSystemBarsLightIcons: () -> Unit,
  onResetSystemBarsIcons: () -> Unit,
  modifier: Modifier = Modifier,
  context: Context = LocalContext.current,
  viewModel: PlayerViewModel = hiltViewModel(),
) {
  val audioState by viewModel.audioState.collectAsStateWithLifecycle()
  val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
  //val scope = CoroutineScope(defaultDispatcher + SupervisorJob())

  val playingQueue = if (viewModel.playingQueue.isEmpty()) {
    listOf(Song.EXAMPLE)
  } else {
    viewModel.playingQueue
  }

  val currentSong = if (viewModel.playingQueue.isEmpty()) {
    Song.EXAMPLE
  } else {
    viewModel.playingQueue[audioState.currentMediaIndex]
  }

  var dominantColor: Int by remember { mutableIntStateOf(0) }
  var gradientColors by remember { mutableStateOf(listOf(Color(dominantColor), TroonaColor.Black)) }

  LaunchedEffect(currentSong.albumArt) {
    var palette: Palette?

    // Get dominant colors from the image using Palette
    withContext(Dispatchers.IO) {
      palette = Palette.from(currentSong.albumArt.asArtworkBitmap(context)!!).generate()
    }

    // Create a gradient from the dominant colors
    gradientColors = run {
      dominantColor = palette!!.getDominantColor(TroonaColor.PrimaryColor.toArgb())
      return@run listOf(Color(dominantColor), TroonaColor.Black)
    }

    // Animate the gradient colors
  }

  LaunchedEffect(isPlayerOpened, onSetSystemBarsLightIcons, onResetSystemBarsIcons) {
    if (isPlayerOpened) onSetSystemBarsLightIcons() else onResetSystemBarsIcons()
  }

  FullPlayerContent(
    audioState = audioState,
    currentSong = currentSong,
    playingQueue = playingQueue,
    gradientColors = gradientColors,
    currentPosition = currentPosition,
    onSkipPrevious = { viewModel.onEvent(UIEvents.SeekToPrevious) },
    onPlayPause = {
      if (audioState.isPlaying) {
        viewModel.onEvent(UIEvents.Pause)
      } else {
        viewModel.onEvent(UIEvents.Play)
      }
    },
    onSkipNext = { viewModel.onEvent(UIEvents.SeekToNext) },
    onSkipTo = { viewModel.onEvent(UIEvents.SeekTo(convertToPosition(it, currentSong.duration))) },
    modifier = modifier
  )
}


@Composable
private fun FullPlayerContent(
  audioState: AudioState,
  currentSong: Song,
  playingQueue: List<Song>,
  gradientColors: List<Color>,
  currentPosition: Long,
  modifier: Modifier = Modifier,
  onLike: () -> Unit = {},
  onSkipTo: (Float) -> Unit,
  onShuffle: () -> Unit = {},
  onRepeat: () -> Unit = {},
  onSkipPrevious: () -> Unit,
  onPlayPause: () -> Unit,
  onSkipNext: () -> Unit,
  isFavorite: Boolean = false,
) {
  val largeRadialGradient = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
      val biggerDimension = maxOf(size.height, size.width)
      return RadialGradientShader(
        colors = gradientColors,
        center = size.center,
        radius = biggerDimension,
        colorStops = listOf(0f, 0.95f)
      )
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(largeRadialGradient)
      .padding(top = MaterialTheme.spacing.large)
  ) {
   /* Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
    Box(
      modifier = Modifier
        .width(MaterialTheme.spacing.large)
        .height(MaterialTheme.spacing.extraSmall)
        .clip(RoundedCornerShape(MaterialTheme.spacing.extraSmall))
        .background(TroonaColor.WhiteAlpha02)
        .align(Alignment.CenterHorizontally)
    )*/
    //Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    Box(
      modifier = Modifier.padding(
        horizontal = PlayerScreenPadding, vertical = MaterialTheme.spacing.medium
      )
    ) {
      TroonaArtwork(
        modifier = modifier.aspectRatio(1f),
        artworkUri = currentSong.albumArt,
        shape = RoundedCornerShape(MaterialTheme.spacing.smallMedium),
        elevation = MaterialTheme.spacing.smallMedium,
        contentDescription = currentSong.title
      )
    }
    Row(
      modifier = Modifier
        .padding(start = PlayerScreenPadding)
        .fillMaxWidth(),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
      ) {
        SingleLineText(
          text = currentSong.title,
          shouldUseMarquee = audioState.isPlaying,
          fontSize = 24.sp,
          color = Color.White,
        )
        SingleLineText(
          text = currentSong.artistName,
          shouldUseMarquee = audioState.isPlaying,
          fontSize = 20.sp,
          color = TroonaColor.WhiteAlpha08,
          fontWeight = FontWeight.SemiBold,
        )
      }

      Row(
        modifier = Modifier.padding(
          start = MaterialTheme.spacing.small, end = MaterialTheme.spacing.medium
        ),
      ) {
        IconButton(modifier = Modifier.size(35.dp), onClick = {}) {
          Icon(
            painter = painterResource(id = if (isFavorite) TroonaIcons.Favorite.resourceId else TroonaIcons.FavoriteBorder.resourceId),
            contentDescription = "Favorite",
            tint = Color.White
          )
        }
        IconButton(modifier = Modifier.size(35.dp), onClick = {}) {
          Icon(
            painter = painterResource(id = TroonaIcons.MoreVert.resourceId),
            contentDescription = "More Options",
            tint = Color.White
          )
        }
      }
    }
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    Column(
      modifier = modifier
        .padding(horizontal = PlayerScreenPadding)
        .fillMaxWidth()
    ) {

      SeekBar(
        currentPosition = currentPosition,
        duration = currentSong.duration,
        onSkipTo = onSkipTo,
        modifier = Modifier,
      )

      Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        TroonaIconButton(
          onClick = onShuffle, modifier = Modifier.size(24.dp)
        ) {
          Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = TroonaIcons.Shuffle.resourceId),
            contentDescription = "Shuffle",
            tint = TroonaColor.WhiteAlpha04
          )
        }
        TroonaIconButton(
          onClick = onSkipPrevious,
          modifier = Modifier.size(55.dp),
          rippleRadius = 32.dp,
        ) {
          Icon(
            modifier = Modifier.size(55.dp),
            painter = painterResource(id = TroonaIcons.FastRewind.resourceId),
            contentDescription = "Skip Previous",
            tint = Color.White
          )
        }
        TroonaIconButton(
          onClick = onPlayPause,
          modifier = Modifier
            .size(70.dp)
            .background(brush = SolidColor(Color.White), shape = CircleShape, alpha = DefaultAlpha),
          rippleRadius = 35.dp,
        ) {
          Icon(
            modifier = Modifier.size(55.dp),
            painter = painterResource(id = if (audioState.isPlaying) TroonaIcons.Pause.resourceId else TroonaIcons.Play.resourceId),
            contentDescription = "Play/Pause",
            tint = Color.White
          )
        }
        TroonaIconButton(
          onClick = onSkipNext,
          modifier = Modifier.size(55.dp),
          rippleRadius = 32.dp,
        ) {
          Icon(
            modifier = Modifier.size(55.dp),
            painter = painterResource(id = TroonaIcons.FastForward.resourceId),
            contentDescription = "Skip Next",
            tint = Color.White
          )
        }
        TroonaIconButton(
          onClick = onRepeat, modifier = Modifier.size(24.dp)
        ) {
          Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = TroonaIcons.Repeat.resourceId),
            contentDescription = "Repeat Mode",
            tint = Color.White
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@UnstableApi
@Composable
fun FullPlayerPreview() {
  FullPlayer(
    isPlayerOpened = true,
    onSetSystemBarsLightIcons = {},
    onResetSystemBarsIcons = {},
    modifier = Modifier.fillMaxSize(),
  )
}

suspend fun Uri.asArtworkBitmap(context: Context): Bitmap? {
  val request = ImageRequest.Builder(context).data(this).allowHardware(false)
    .placeholder(TroonaIcons.Music.resourceId).error(TroonaIcons.Music.resourceId).build()

  val drawable = ImageLoader(context).execute(request).drawable
  return drawable?.toBitmap()
}

private val PlayerScreenPadding = 20.dp
private const val DefaultAlpha = 0.14f
private const val DefaultTextAlpha = 0.9f
private const val DefaultSliderAlpha = 0.5f
