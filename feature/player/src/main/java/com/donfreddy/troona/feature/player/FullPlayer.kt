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
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.designsystem.images.TroonaArtwork
import com.donfreddy.troona.core.designsystem.theme.TroonaColor
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.media.AudioState
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.ui.song.asDuration
import com.donfreddy.troona.feature.player.util.asFormattedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

@UnstableApi
@Composable
fun FullPlayer(
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

  var dominantColor: Int by remember { mutableIntStateOf(Color.Black.toArgb()) }
  var textColor: Int by remember { mutableIntStateOf(Color.White.toArgb()) }
  var textColor2: Int by remember { mutableIntStateOf(Color.White.toArgb()) }

  val blendedColor = remember { Animatable(Color(dominantColor)) }

  var gradientColors by remember {
    mutableStateOf(
      listOf(
        Color(Color.Black.toArgb()), Color(Color.White.toArgb())
      )
    )
  }

  LaunchedEffect(currentSong.albumArt) {
    var palette: Palette?

    // Get dominant colors from the image using Palette
    withContext(Dispatchers.IO) {
      palette = Palette.from(currentSong.albumArt.asArtworkBitmap(context)!!).generate()
    }

    // Create a gradient from the dominant colors
    gradientColors = run {
      dominantColor = palette!!.dominantSwatch?.rgb ?: Color.Black.toArgb()
      textColor = palette!!.dominantSwatch?.bodyTextColor ?: Color.White.toArgb()
      textColor2 = palette!!.dominantSwatch?.titleTextColor ?: Color.White.toArgb()
      return@run listOf(Color(dominantColor), Color(textColor))
    }
  }

  FullPlayerContent(
    audioState = audioState,
    currentSong = currentSong,
    playingQueue = playingQueue,
    gradientColors = gradientColors,
    dominantColor = dominantColor,
    textColor = textColor,
    textColor2 = textColor2,
    currentPosition = audioState.duration,
    modifier = modifier
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FullPlayerContent(
  audioState: AudioState,
  currentSong: Song,
  playingQueue: List<Song>,
  gradientColors: List<Color>,
  dominantColor: Int,
  textColor: Int,
  textColor2: Int,
  currentPosition: Long,
  isFavorite: Boolean = false,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      //.background(brush = Brush.verticalGradient(gradientColors))
      .background(Color(dominantColor).copy(alpha = 0.8f))
  ) {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
    Box(
      modifier = Modifier
        .width(34.dp)
        .height(4.dp)
        .clip(RoundedCornerShape(4.dp))
        //.shadow(elevation = 0.dp, shape = RoundedCornerShape(8.dp))
        .background(Color.White.copy(alpha = 0.15f))
        .align(Alignment.CenterHorizontally)
    )
    Box(
      modifier = Modifier.padding(
        horizontal = PlayerScreenPadding, vertical = MaterialTheme.spacing.medium
      )
    ) {
      TroonaArtwork(
        modifier = modifier.aspectRatio(1f),
        artworkUri = currentSong.albumArt,
        shape = RoundedCornerShape(MaterialTheme.spacing.smallMedium),
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
          fontSize = 18.sp,
          color = Color.White.copy(0.8f),
          fontWeight = FontWeight.SemiBold,
        )
      }

      Row(
        modifier = Modifier.padding(
          start = MaterialTheme.spacing.small,
          end = MaterialTheme.spacing.medium
        ),
      ) {
        IconButton(modifier = Modifier.size(35.dp), onClick = {}) {
          Icon(
            painter = painterResource(id = if (isFavorite) TroonaIcons.Favorite.resourceId else TroonaIcons.FavoriteBorder.resourceId),
            contentDescription = "Play/Pause",
            tint = Color.White
            // modifier = Modifier.size(30.dp)
          )
        }
        IconButton(modifier = Modifier.size(35.dp), onClick = {}) {
          Icon(
            painter = painterResource(id = TroonaIcons.MoreVert.resourceId),
            contentDescription = "More Options",
            tint = Color.White
            //modifier = Modifier.size(30.dp)
          )
        }
      }
    }
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.smallMedium))
    Column(
      modifier = modifier
        .padding(horizontal = PlayerScreenPadding)
        .fillMaxWidth()
    ) {
      CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Slider(
          modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .background(Color.Black),
          value = 0.5f,
          onValueChange = {},
          colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color.White,
            inactiveTrackColor = Color.White.copy(alpha = 0.5f),
            activeTickColor = Color.White,
            inactiveTickColor = Color.White.copy(alpha = 0.5f)
          )
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "0:00",
          style = MaterialTheme.typography.body2,
          fontWeight = FontWeight.SemiBold,
        )
        Text(
          text = "3:00",
          style = MaterialTheme.typography.body2,
          fontWeight = FontWeight.SemiBold,
        )
      }

      Spacer(modifier = Modifier.height(MaterialTheme.spacing.smallMedium))

      Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        IconButton(
          onClick = {},
          modifier = Modifier.size(20.dp)
        ) {
          Icon(
            painter = painterResource(id = TroonaIcons.Shuffle.resourceId),
            contentDescription = "Shuffle",
            tint = Color.White
          )
        }
        IconButton(
          onClick = {},
          modifier = Modifier.size(40.dp)
        ) {
          Icon(
            painter = painterResource(id = TroonaIcons.SkipPrevious.resourceId),
            contentDescription = "Skip Previous",
            tint = Color.White
          )
        }
        IconButton(
          onClick = {},
          modifier = Modifier
           // .size(60.dp)
            .background(brush = SolidColor(Color.White), shape = CircleShape, alpha = 0.2f)
        ) {
          Icon(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = if (audioState.isPlaying) TroonaIcons.Pause.resourceId else TroonaIcons.Play.resourceId),
            contentDescription = "Play/Pause",
            tint = Color.White
          )
        }
        IconButton(
          onClick = {},
          modifier = Modifier.size(40.dp)
        ) {
          Icon(
            painter = painterResource(id = TroonaIcons.SkipNext.resourceId),
            contentDescription = "Skip Next",
            tint = Color.White
          )
        }
        IconButton(
          onClick = {},
          modifier = Modifier.size(20.dp)
        ) {
          Icon(
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
  FullPlayer()
}

suspend fun Uri.asArtworkBitmap(context: Context): Bitmap? {
  val request = ImageRequest.Builder(context).data(this).allowHardware(false)
    .placeholder(TroonaIcons.Music.resourceId).error(TroonaIcons.Music.resourceId).build()

  val drawable = ImageLoader(context).execute(request).drawable
  return drawable?.toBitmap()
}

fun createGradientBrush(colors: List<Color>): Brush {
  return Brush.verticalGradient(
    colors = colors,
  )
}

@Composable
fun AnimateColorTransition(dominantColor: Int, blendedColor: Animatable<Color, AnimationVector4D>) {
  val infiniteTransition = rememberInfiniteTransition(label = "ColorTransition")
  infiniteTransition.animateColor(
    initialValue = Color(Color.Black.toArgb()),
    targetValue = Color(dominantColor),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse,
      initialStartOffset = StartOffset(Random.nextInt(0, 2000))
    ),
    label = "ColorTransition"
  )
}

private val PlayerScreenPadding = 20.dp