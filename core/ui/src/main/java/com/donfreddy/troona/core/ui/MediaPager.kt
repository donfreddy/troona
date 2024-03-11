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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.core.designsystem.component.TroonaScrollableTabRow
import com.donfreddy.troona.core.designsystem.component.TroonaTab
import com.donfreddy.troona.core.model.data.Song
import com.donfreddy.troona.core.ui.song.SongCardItems
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
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold,
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
          SongCardItems(
            songs = songs,
            currentPlayingSongId = currentPlayingSongId,
            onClick = onSongClick
          )
        }
      }
    }
  }
}

internal enum class MediaTab(@StringRes val titleRes: Int) {
  // SUGGESTED(R.string.core_ui_suggested_title),
  SONGS(R.string.core_ui_songs_title),
  ARTISTS(R.string.core_ui_artists_title),
  ALBUMS(R.string.core_ui_albums_title),
}