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

package com.donfreddy.troona.core.ui.song

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donfreddy.troona.core.model.data.Song

/**
 * [SongCardItems] use for displaying a [List] of [SongCard] backed by a list of
 * [Song]s.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongCardItems(
  songs: List<Song>,
  currentPlayingSongId: String,
  onClick: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  if (songs.isNotEmpty()) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
      itemsIndexed(items = songs, key = { _, song -> song.id }) { index, song ->
        SongCard(
          modifier = modifier.animateItemPlacement(),
          song = song,
          isPlaying = song.id.toString() == currentPlayingSongId,
          onClick = { onClick(index) },
        )
      }
    }
  } else {
    EmptyContent()
  }
}

/**
 * Extension function for displaying a [List] of [SongCard] backed by a list of
 * [Song]s.
 */
@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.songCardItems(
  songs: List<Song>,
  currentPlayingSongId: String,
  onClick: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  if (songs.isNotEmpty()) {
    itemsIndexed(items = songs, key = { _, song -> song.id }) { index, song ->
      SongCard(
        modifier = modifier.animateItemPlacement(),
        song = song,
        isPlaying = song.id.toString() == currentPlayingSongId,
        onClick = { onClick(index) },
      )
    }
  } else {
    item {
      EmptyContent()
    }
  }
}

@Composable
internal fun EmptyContent() {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
      Text(text = "No songs found", fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "Please add some songs to your sd card or internal storage",
        fontSize = 12.sp,
        color = Color.Gray,
        fontStyle = FontStyle.Italic
      )
    }
  }
}