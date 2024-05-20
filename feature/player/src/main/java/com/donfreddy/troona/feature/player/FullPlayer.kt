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
import android.graphics.RuntimeShader
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import com.donfreddy.troona.core.designsystem.theme.TroonaTheme
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.media.AudioState
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.ui.tooling.DevicePreviews
import com.donfreddy.troona.feature.player.components.TroonaSlider
import com.donfreddy.troona.feature.player.util.asFormattedString
import com.donfreddy.troona.feature.player.util.convertToPosition
import com.donfreddy.troona.feature.player.util.convertToProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language
import java.time.Duration

@UnstableApi
@Composable
fun FullPlayer(
  isPlayerOpened: Boolean,
  onSetSystemBarsLightIcons: () -> Unit,
  onResetSystemBarsIcons: () -> Unit,
  modifier: Modifier = Modifier,
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

  LaunchedEffect(isPlayerOpened, onSetSystemBarsLightIcons, onResetSystemBarsIcons) {
    if (isPlayerOpened) onSetSystemBarsLightIcons() else onResetSystemBarsIcons()
  }

  FullPlayer(
    audioState = audioState,
    currentSong = currentSong,
    playingQueue = playingQueue,
    currentPosition = currentPosition,
    playerControlActions = PlayerControlActions(
      onPlay = viewModel::onPlay,
      onPause = viewModel::onPause,
      onSkipPrevious = viewModel::onSkipPrevious,
      onSkipNext = viewModel::onSkipNext,
      onSkipTo = { (viewModel::onSkipTo)(convertToPosition(it.toFloat(), currentSong.duration)) },
      onSkipToIndex = viewModel::onSkipToIndex,
      onShuffle = viewModel::onShuffle,
      onRepeat = viewModel::onRepeat,
    ),
    modifier = modifier,
  )
}

@Composable
private fun FullPlayer(
  audioState: AudioState,
  currentSong: Song,
  playingQueue: List<Song>,
  currentPosition: Long,
  playerControlActions: PlayerControlActions,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier) {
    PlayerBackground(currentSong, modifier = modifier.fillMaxSize())

    FullPlayerContent(
      audioState = audioState,
      currentSong = currentSong,
      playingQueue = playingQueue,
      currentPosition = currentPosition,
      playerControlActions = playerControlActions,
      modifier = modifier
    )
  }
}

@Composable
private fun PlayerBackground(
  currentSong: Song,
  modifier: Modifier,
  context: Context = LocalContext.current,
) {
  var dominantColor: Int by remember { mutableIntStateOf(0) }
  //var bodyTextColor: Int by remember { mutableIntStateOf(0) }
  var gradientColors by remember {
    mutableStateOf(listOf(Color(dominantColor), TroonaColor.Black))
  }

  LaunchedEffect(currentSong.albumArt) {
    var palette: Palette?

    // Get dominant colors from the image using Palette
    withContext(Dispatchers.IO) {
      palette = Palette.from(currentSong.albumArt.asArtworkBitmap(context)!!).generate()
    }

    // Create a gradient from the dominant colors
    gradientColors = run {
      dominantColor = palette!!.getDominantColor(TroonaColor.PrimaryColor.toArgb())
      //bodyTextColor = palette!!.vibrantSwatch?.rgb!!
      return@run listOf(Color(dominantColor), TroonaColor.Black)
    }

    //Todo: Animate the gradient colors when the image changes
  }

  val largeVerticalGradient = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
      val biggerDimension = maxOf(size.height, size.width)
      return LinearGradientShader(
        colors = gradientColors,
        from = Offset(0f, -biggerDimension),
        to = Offset(biggerDimension * 1.6f, size.height),
        colorStops = listOf(0.4f, 1f)
      )
    }
  }

  /* fun verticalGradient(
      vararg colorStops: Pair<Float, Color>,
      startY: Float = 0f,
      endY: Float = Float.POSITIVE_INFINITY,
      tileMode: TileMode = TileMode.Clamp
    ): Brush = linearGradient(
      *colorStops,
      start = Offset(0.0f, startY),
      end = Offset(0.0f, endY),
      tileMode = tileMode
    )*/

  Box(modifier = modifier.background(largeVerticalGradient))
}


@Composable
private fun FullPlayerContent(
  audioState: AudioState,
  currentSong: Song,
  playingQueue: List<Song>,
  currentPosition: Long,
  playerControlActions: PlayerControlActions,
  modifier: Modifier = Modifier,
  isFavorite: Boolean = false,
) {

  Column(
    modifier = modifier
      .fillMaxSize()
      .systemBarsPadding()
      .padding(top = MaterialTheme.spacing.extraMedium)
  ) {
    PlayerArtwork(
      currentSong = currentSong,
      isPlaying = audioState.isPlaying,
      modifier = Modifier
        .padding(start = PlayerScreenPadding, end = PlayerScreenPadding)
        .fillMaxWidth()
    )
    Row(
      modifier = Modifier
        .padding(
          start = PlayerScreenPadding,
          top = MaterialTheme.spacing.extraMedium,
          bottom = MaterialTheme.spacing.medium,
        )
        .fillMaxWidth(),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      SongInfo(
        currentSong = currentSong,
        isPlaying = audioState.isPlaying,
        modifier = Modifier.weight(1f)
      )
      FavoriteAndMore(
        onFavoriteClick = {},
        onMoreClick = {},
        isFavorite = isFavorite,
        modifier = Modifier
      )
    }
    Column(
      modifier = modifier
        .padding(horizontal = PlayerScreenPadding)
        .fillMaxWidth()
    ) {
      PlayerSlider(
        currentPosition = currentPosition,
        duration = currentSong.duration,
        onSkipTo = {},
        modifier = Modifier.fillMaxWidth()
      )
      PlayerButtons(
        hasNext = true,
        isPlaying = audioState.isPlaying,
        onPlay = playerControlActions.onPlay,
        onPause = playerControlActions.onPause,
        onSkipPrevious = playerControlActions.onSkipPrevious,
        onSkipNext = playerControlActions.onSkipNext,
        onShuffle = playerControlActions.onShuffle,
        onRepeat = playerControlActions.onRepeat,
        modifier = Modifier.fillMaxWidth()
      )
    }

    Spacer(modifier = Modifier.weight(1.0f))

    BottomActions(
      onLyricsClick = {},
      onQueueClick = {},
      modifier = Modifier
        .padding(horizontal = PlayerScreenPadding)
        .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
    Box(
      modifier = modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(TroonaColor.White.copy(alpha = 0.05f))
    )
  }
}

@Composable
private fun PlayerArtwork(
  currentSong: Song,
  isPlaying: Boolean,
  modifier: Modifier = Modifier
) {
  val sizeScale: Float by animateFloatAsState(
    targetValue = if (isPlaying) 1f else 0.8f,
    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
    label = "ScaleAnimation"
  )

  Box(
    modifier = modifier
      .aspectRatio(1f)
      .clip(MaterialTheme.shapes.large)
      .graphicsLayer(scaleX = sizeScale, scaleY = sizeScale),
  ) {
    TroonaArtwork(
      artworkUri = currentSong.albumArt,
      elevation = MaterialTheme.spacing.extraSmall,
      shape = MaterialTheme.shapes.large,
      contentDescription = currentSong.title
    )
  }
}

@Composable
private fun SongInfo(
  currentSong: Song,
  isPlaying: Boolean,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
    SingleLineText(
      text = currentSong.title,
      fontSize = 24.sp,
      color = Color.White,
      shouldUseMarquee = isPlaying,
    )
    SingleLineText(
      text = currentSong.artistName,
      fontSize = 20.sp,
      color = TroonaColor.WhiteAlpha08,
      shouldUseMarquee = isPlaying,
      fontWeight = FontWeight.SemiBold,
    )
  }
}

@Composable
private fun FavoriteAndMore(
  onFavoriteClick: () -> Unit,
  onMoreClick: () -> Unit,
  isFavorite: Boolean,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .padding(
        start = MaterialTheme.spacing.small,
        end = MaterialTheme.spacing.medium,
      ),
  ) {
    IconButton(modifier = Modifier.size(35.dp), onClick = onFavoriteClick) {
      Icon(
        painter = painterResource(id = if (isFavorite) TroonaIcons.Favorite.resourceId else TroonaIcons.FavoriteBorder.resourceId),
        contentDescription = "Favorite",
        tint = Color.White
      )
    }
    IconButton(modifier = Modifier.size(35.dp), onClick = onMoreClick) {
      Icon(
        painter = painterResource(id = TroonaIcons.MoreVert.resourceId),
        contentDescription = "More Options",
        tint = Color.White
      )
    }
  }
}

@Composable
fun PlayerSlider(
  currentPosition: Long,
  duration: Long,
  onSkipTo: (Float) -> Unit,
  modifier: Modifier = Modifier,
) {

  val progress by animateFloatAsState(
    targetValue = convertToProgress(count = currentPosition, total = duration),
    label = "ProgressAnimation"
  )

  Column(
    modifier = modifier
      .padding(vertical = MaterialTheme.spacing.medium)
      .fillMaxWidth(),
  ) {
    TroonaSlider(
      value = progress,
      onValueChanged = onSkipTo,
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
    Row(
      modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = currentPosition.asFormattedString(),
        style = MaterialTheme.typography.body2.copy(
          fontWeight = FontWeight.W700,
          fontSize = 11.sp,
          color = TroonaColor.WhiteAlpha04,
        ),
      )
      Text(
        text = "−${(duration - currentPosition).asFormattedString()}",
        style = MaterialTheme.typography.body2.copy(
          fontWeight = FontWeight.W700,
          fontSize = 11.sp,
          color = TroonaColor.WhiteAlpha04,
        ),
      )
    }
  }
}

@Composable
private fun PlayerButtons(
  hasNext: Boolean,
  isPlaying: Boolean,
  onPlay: () -> Unit,
  onPause: () -> Unit,
  onSkipPrevious: () -> Unit,
  onSkipNext: () -> Unit,
  onShuffle: () -> Unit,
  onRepeat: () -> Unit,
  modifier: Modifier = Modifier,
  smallIconSize: Dp = 24.dp,
  sideIconSize: Dp = 55.dp,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    val playPauseIcon = if (isPlaying) TroonaIcons.Pause else TroonaIcons.Play

    TroonaIconButton(onClick = onShuffle, modifier = Modifier.size(smallIconSize)) {
      Icon(
        modifier = Modifier.size(sideIconSize),
        painter = painterResource(id = TroonaIcons.Shuffle.resourceId),
        contentDescription = "Shuffle",
        tint = TroonaColor.WhiteAlpha04
      )
    }
    TroonaIconButton(
      onClick = onSkipPrevious,
      modifier = Modifier.size(sideIconSize),
      rippleRadius = 32.dp,
    ) {
      Icon(
        modifier = Modifier.size(sideIconSize),
        painter = painterResource(id = TroonaIcons.FastRewind.resourceId),
        contentDescription = "Skip Previous",
        tint = Color.White
      )
    }
    TroonaIconButton(
      onClick = if (isPlaying) onPause else onPlay,
      modifier = Modifier
        .size(70.dp)
        .background(
          brush = SolidColor(Color.White),
          shape = CircleShape,
          alpha = DefaultAlpha
        ),
      rippleRadius = 35.dp,
    ) {
      Icon(
        modifier = Modifier.size(sideIconSize),
        painter = painterResource(id = playPauseIcon.resourceId),
        contentDescription = "Play/Pause",
        tint = Color.White
      )
    }
    TroonaIconButton(
      onClick = onSkipNext,
      enabled = hasNext,
      modifier = Modifier.size(sideIconSize),
      rippleRadius = 32.dp,
    ) {
      Icon(
        modifier = Modifier.size(sideIconSize),
        painter = painterResource(id = TroonaIcons.FastForward.resourceId),
        contentDescription = "Skip Next",
        tint = Color.White
      )
    }
    TroonaIconButton(onClick = onRepeat, modifier = Modifier.size(smallIconSize)) {
      Icon(
        modifier = Modifier.size(smallIconSize),
        painter = painterResource(id = TroonaIcons.Repeat.resourceId),
        contentDescription = "Repeat Mode",
        tint = Color.White
      )
    }
  }
}

@Composable
private fun BottomActions(
  onLyricsClick: () -> Unit,
  onQueueClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth().padding(horizontal = MaterialTheme.spacing.extraLarge),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    TroonaIconButton(onClick = onLyricsClick, modifier = Modifier.size(24.dp)) {
      Icon(
        modifier = Modifier.size(24.dp),
        painter = painterResource(id = TroonaIcons.Lyrics.resourceId),
        contentDescription = "Lyrics",
        tint = Color.White
      )
    }
    TroonaIconButton(onClick = onQueueClick, modifier = Modifier.size(24.dp)) {
      Icon(
        modifier = Modifier.size(24.dp),
        painter = painterResource(id = TroonaIcons.QueueMusic.resourceId),
        contentDescription = "Queue",
        tint = Color.White
      )
    }
  }
}

/**
 * Wrapper around all actions for the player controls.
 */
data class PlayerControlActions(
  val onPlay: () -> Unit,
  val onPause: () -> Unit,
  val onSkipPrevious: () -> Unit,
  val onSkipNext: () -> Unit,
  val onSkipTo: (position: Long) -> Unit,
  val onSkipToIndex: (index: Int) -> Unit,
  val onShuffle: () -> Unit,
  val onRepeat: () -> Unit,
)

@Preview
@Composable
fun PlayerButtonsPreview() {
  TroonaTheme {
    PlayerButtons(
      hasNext = false,
      isPlaying = false,
      onPlay = {},
      onPause = {},
      onSkipPrevious = {},
      onSkipNext = {},
      onShuffle = {},
      onRepeat = {}
    )
  }
}

@DevicePreviews
@Composable
fun FullPlayerScreenPreview() {
  TroonaTheme {
    FullPlayer(
      audioState = AudioState(),
      currentSong = Song.EXAMPLE,
      playingQueue = listOf(Song.EXAMPLE),
      currentPosition = Duration.ofSeconds(30).toMillis(),
      playerControlActions = PlayerControlActions(
        onPlay = {},
        onPause = {},
        onSkipPrevious = {},
        onSkipNext = {},
        onSkipTo = {},
        onSkipToIndex = {},
        onShuffle = {},
        onRepeat = {},
      ),
    )
  }
}

suspend fun Uri.asArtworkBitmap(context: Context): Bitmap? {
  val request = ImageRequest.Builder(context).data(this).allowHardware(false)
    .placeholder(TroonaIcons.Music.resourceId).error(TroonaIcons.Music.resourceId).build()

  val drawable = ImageLoader(context).execute(request).drawable
  return drawable?.toBitmap()
}

private val PlayerScreenPadding = 24.dp
private const val DefaultAlpha = 0.14f
private const val DefaultTextAlpha = 0.9f
private const val DefaultSliderAlpha = 0.5f
