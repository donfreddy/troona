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

package com.donfreddy.troona.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.core.designsystem.component.TroonaScrollableTabRow
import com.donfreddy.troona.core.designsystem.component.TroonaTab
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.ui.song.SongCardItems
import com.donfreddy.troona.core.ui.song.songCardItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPager(
  songs: List<Song>,
  currentPlayingSongId: String,
  onSongClick: (Int) -> Unit,
  modifier: Modifier = Modifier,
  coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
  Column(modifier) {
    val tabs = remember { MediaTab.entries.toTypedArray() }
    val pagerState = rememberPagerState(pageCount = tabs::size)
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    TroonaScrollableTabRow(selectedTabIndex = selectedTabIndex.value) {
      tabs.forEachIndexed { index, tab ->
        TroonaTab(
          text = {
            Text(
              text = stringResource(id = tab.titleRes),
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              lineHeight = 1.sp,
              letterSpacing = 0.sp,
              color = if (selectedTabIndex.value == index) MaterialTheme.colors.primary else Color.Unspecified
            )
          },
          selected = selectedTabIndex.value == index,
          onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
        )
      }
    }

    HorizontalPager(
      modifier = Modifier.fillMaxSize(),
      state = pagerState,
      verticalAlignment = Alignment.Top
    ) { index ->
      when (tabs[index]) {
        // MediaTab.SUGGESTED -> {SuggestedScreen()}

        MediaTab.SONGS, MediaTab.ARTISTS, MediaTab.ALBUMS -> {
          LazyColumn(modifier = modifier.fillMaxSize()) {
            item {
              MediaPagerHeader()
            }
            songCardItems(
              songs = songs,
              currentPlayingSongId = currentPlayingSongId,
              onClick = onSongClick
            )
          }
        }
      }
    }
  }
}

@Composable
private fun MediaPagerHeader(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(MaterialTheme.spacing.small)
  ) {
    Row(
      modifier = modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "360 Songs",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 1.sp,
        letterSpacing = 0.sp,
      )

      SortButton(onClick = {})
    }
  }
}

@Composable
fun SortButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Button(
    modifier = Modifier
      .height(20.dp)
      .clip(RoundedCornerShape(MaterialTheme.spacing.largest)),
    onClick = onClick,
    shape = RoundedCornerShape(MaterialTheme.spacing.largest),
    contentPadding = PaddingValues(MaterialTheme.spacing.default),
    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
  ) {
    Row(
      modifier = modifier.padding(horizontal = 4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        painter = painterResource(id = TroonaIcons.Title.resourceId),
        contentDescription = "Title",
        modifier = Modifier.size(MaterialTheme.spacing.medium),
      )
      Spacer(modifier = modifier.width(2.dp))
      Text(
        text = "Title",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 1.sp,
        letterSpacing = 0.sp,
      )
      Spacer(modifier = modifier.width(2.dp))
      Icon(
        Icons.Default.KeyboardArrowDown,
        modifier = Modifier.size(16.dp),
        contentDescription = null
      )
    }
  }
}

internal enum class MediaTab(@StringRes val titleRes: Int) {
  // SUGGESTED(R.string.core_ui_suggested_title),
  SONGS(R.string.core_ui_songs_title),
  ARTISTS(R.string.core_ui_artists_title),
  ALBUMS(R.string.core_ui_albums_title),
}

@Preview
@Composable
fun SortButtonPreview() {
  SortButton(onClick = { /*TODO*/ })
}