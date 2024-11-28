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

package com.donfreddy.troona.feature.artist

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.donfreddy.troona.core.designsystem.component.TroonaTopBar
import com.donfreddy.troona.core.designsystem.images.TroonaArtwork
import com.donfreddy.troona.core.designsystem.theme.Nunito
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.model.data.Album
import com.donfreddy.troona.core.model.data.Artist
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.network.ApiResponse
import com.donfreddy.troona.core.network.models.LastFmArtist
import com.donfreddy.troona.core.ui.common.SongCoverSize
import com.donfreddy.troona.core.ui.component.PlayOrShuffleButtons
import com.donfreddy.troona.core.ui.component.SongItem
import com.donfreddy.troona.core.ui.component.TroonaDivider
import com.donfreddy.troona.core.ui.util.HelpersUtil
import com.donfreddy.troona.core.ui.util.HelpersUtil.getArtistInfoStringWithDuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@UnstableApi
@Composable
internal fun ArtistScreen(
  onBackClick: () -> Unit,
  onAlbumClick: (Long) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: ArtistViewModel = hiltViewModel(),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val audioState by viewModel.audioState.collectAsStateWithLifecycle()

  when (val uiState = state) {
    ArtistUiState.Loading -> Unit

    is ArtistUiState.Success -> {
      // Collect artist info separately
      val artistInfo by viewModel.getArtistInfo(uiState.artist.name).collectAsState(initial = null)

      ArtistScreen(
        artist = uiState.artist,
        onBackClick = onBackClick,
        onSongClick = { startIndex ->
          (viewModel::play)(uiState.artist.songs, startIndex)
        },
        onAlbumClick = onAlbumClick,
        artistInfo = artistInfo,
        currentPlayingSongId = audioState.currentMediaId,
        modifier = modifier
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtistScreen(
  artist: Artist,
  onBackClick: () -> Unit,
  onSongClick: (Int) -> Unit,
  onAlbumClick: (Long) -> Unit,
  artistInfo: LastFmArtist?,
  currentPlayingSongId: String,
  modifier: Modifier = Modifier,
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  var isContextual by remember { mutableStateOf(false) }

  Scaffold(
    Modifier
      .fillMaxSize()
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TroonaTopBar(
        searchWidgetState = { /*TODO*/ },
        onBackClick = onBackClick,
        scrollBehavior = scrollBehavior,
      )
    },

    ) { innerPadding ->
    val scrollState = rememberScrollState()

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(scrollState),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
      ArtistHeaderSection(artist = artist)

      PlayOrShuffleButtons(
        onPlay = { isContextual = !isContextual },
        onShuffle = { /*TODO*/ },
      )

      TroonaDivider()

      ArtistSongsSection(songs = artist.songs,
        currentPlayingSongId = currentPlayingSongId,
        onSongClick = onSongClick,
        onMoreClick = { /*TODO*/ })

      ArtistAlbumsSection(albums = artist.albums, onAlbumClick = onAlbumClick)

      artistInfo?.let { artistInfo ->
        if (artistInfo.artist.bio == null) return@let
        val bioContent = artistInfo.artist.bio!!.content

        if (bioContent != null && bioContent.trim { it <= ' ' }.isNotEmpty()) {
          Column(
            modifier = Modifier
              .padding(horizontal = MaterialTheme.spacing.medium),
          ) {
            Text(
              text = "Biographie de ${artist.name}",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.W700, fontFamily = Nunito
              ),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            ExpandableText("${artistInfo.artist.bio?.content}")
            if (artistInfo.artist.stats?.listeners?.isNotEmpty() == true) {
              Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
              Row {
                Column {
                  Text(
                    text = "Auditors",
                    style = MaterialTheme.typography.bodyLarge.copy(
                      fontWeight = FontWeight.W600, fontFamily = Nunito
                    ),
                  )
                  Text(
                    text = HelpersUtil.formatValue(artistInfo.artist.stats!!.listeners!!.toFloat()),
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = FontWeight.W400, fontFamily = Nunito
                    ),
                  )
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Column {
                  Text(
                    text = "Scrobble",
                    style = MaterialTheme.typography.bodyLarge.copy(
                      fontWeight = FontWeight.W600, fontFamily = Nunito
                    ),
                  )
                  Text(
                    text = HelpersUtil.formatValue(artistInfo.artist.stats!!.playCount!!.toFloat()),
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = FontWeight.W400, fontFamily = Nunito
                    ),
                  )
                }
              }
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(100.dp))
    }
  }
}

@Composable
fun ArtistHeaderSection(
  artist: Artist, context: Context = LocalContext.current
) {
  TroonaArtwork(
    modifier = Modifier
      .size(SongCoverSize.MEDIUM.value)
      .padding(top = MaterialTheme.spacing.extraSmall),
    artworkUri = artist.songs.first().albumArt,
    shape = MaterialTheme.shapes.large,
    contentDescription = artist.name,
  )
  Column {
    Text(
      text = artist.name,
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(horizontal = MaterialTheme.spacing.extraSmall),
      style = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Bold, fontFamily = Nunito
      ),
      textAlign = TextAlign.Center
    )
    Text(
      text = getArtistInfoStringWithDuration(context, artist),
      modifier = Modifier.align(Alignment.CenterHorizontally),
      style = MaterialTheme.typography.titleMedium.copy(
        color = Color.Gray, fontFamily = Nunito, fontWeight = FontWeight.W600
      )
    )
  }
}

@Composable
fun ArtistSongsSection(
  songs: List<Song>,
  currentPlayingSongId: String,
  onSongClick: (Int) -> Unit,
  onMoreClick: () -> Unit
) {
  val pages = songs.chunked(5)
  val pagerState = rememberPagerState(pageCount = pages::size)

  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
    ) {
      Text(
        text = "Songs",
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          //fontWeight = FontWeight.W600,
          fontFamily = Nunito
        ),
      )
    }
    HorizontalPager(
      state = pagerState,
      pageSpacing = (-MaterialTheme.spacing.extraMedium),
      modifier = Modifier.padding(top = MaterialTheme.spacing.small)
    ) { page ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = MaterialTheme.spacing.small),
      ) {
        pages[page].forEachIndexed { index, song ->
          val songIndex = page * 5 + index
          if (songIndex in songs.indices) {
            SongItem(
              song = song,
              isPlaying = song.id.toString() == currentPlayingSongId,
              onClick = { onSongClick(songIndex) },
              onMoreClick = onMoreClick
            )
          }
        }
      }
    }
  }
}

@Composable
fun ArtistAlbumsSection(
  albums: List<Album>, onAlbumClick: (Long) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
    ) {
      Text(
        text = "Albums",
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          //fontWeight = FontWeight.W600,
          fontFamily = Nunito
        ),
      )
    }
    LazyRow {
      items(items = albums, key = Album::id) { album ->
        val isFist = albums.indexOf(album) == 0
        val isLast = albums.indexOf(album) == albums.size - 1
        Column(
          modifier = Modifier
            .padding(
              start = if (isFist) MaterialTheme.spacing.medium else MaterialTheme.spacing.small,
              end = if (isLast) MaterialTheme.spacing.medium else MaterialTheme.spacing.small,
              top = MaterialTheme.spacing.medium,
            )
            .clickable(onClick = { onAlbumClick(album.id) })
        ) {
          TroonaArtwork(
            modifier = Modifier.size(SongCoverSize.LARGE.value / 2),
            artworkUri = album.songs.first().albumArt,
            shape = MaterialTheme.shapes.medium,
            contentDescription = album.artistName,
          )
          Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
          Text(
            text = album.title,
            modifier = Modifier.width(150.dp),
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.W600, fontFamily = Nunito
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}

@Composable
fun HtmlContentTextView(html: String) {
  AndroidView(factory = { context ->
    TextView(context).apply {
      text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
      textSize = 16f // Text size in SP
      setTextColor(ContextCompat.getColor(context, android.R.color.white)) // Text color
      setTypeface(
        ResourcesCompat.getFont(
          context, com.donfreddy.troona.core.designsystem.R.font.nunito_medium
        )
      )
      // setPadding(16, 16, 16, 16) // Padding in pixels
      //gravity = Gravity.START // Align text to start
    }
  }, update = { textView ->
    textView.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
  })
}

@Composable
fun ExpandableText(
  text: String,
  modifier: Modifier = Modifier,
  minimizedMaxLines: Int = 3,
) {
  var isExpanded by remember { mutableStateOf(false) }
  val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
  var isClickable by remember { mutableStateOf(false) }
  var finalText by remember { mutableStateOf(text) }

  val textLayoutResult = textLayoutResultState.value
  LaunchedEffect(textLayoutResult) {
    if (textLayoutResult == null) return@LaunchedEffect

    when {
      isExpanded -> {
        finalText = "$text Show Less"
      }

      !isExpanded && textLayoutResult.hasVisualOverflow -> {
        val lastCharIndex = textLayoutResult.getLineEnd(minimizedMaxLines - 1)
        val showMoreString = "... Show More"
        val adjustedText =
          text.substring(startIndex = 0, endIndex = lastCharIndex).dropLast(showMoreString.length)
            .dropLastWhile { it == ' ' || it == '.' }

        finalText = "$adjustedText$showMoreString"

        isClickable = true
      }
    }
  }

  Text(
    text = finalText,
    maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
    onTextLayout = { textLayoutResultState.value = it },
    style = MaterialTheme.typography.bodyLarge.copy(fontFamily = Nunito),
    modifier = modifier
      .clickable(enabled = isClickable) { isExpanded = !isExpanded }
      .animateContentSize(),
  )
}

@Preview(showBackground = true, name = "Artist screen")
@Composable
fun FavoritesScreenPreview() {
  ArtistScreen(
    artist = Artist.empty,
    onBackClick = {},
    onSongClick = {},
    onAlbumClick = {},
    artistInfo = null,
    currentPlayingSongId = "",
  )
}