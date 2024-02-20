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

package com.donfreddy.troona.core.model.data

import com.donfreddy.troona.core.model.enums.AlbumSortBy
import com.donfreddy.troona.core.model.enums.ArtistSortBy
import com.donfreddy.troona.core.model.enums.DarkThemeConfig
import com.donfreddy.troona.core.model.enums.GenreSortBy
import com.donfreddy.troona.core.model.enums.PlaybackMode
import com.donfreddy.troona.core.model.enums.SongSortBy
import com.donfreddy.troona.core.model.enums.SortOrder

data class UserData(
  val songSortBy: SongSortBy,
  val songSortOrder: SortOrder,
  val artistSortBy: ArtistSortBy,
  val artistSortOrder: SortOrder,
  val albumSortBy: AlbumSortBy,
  val albumSortOrder: SortOrder,
  val genreSortBy: GenreSortBy,
  val genreSortOrder: SortOrder,
  val ignoreCase: Boolean,
  val darkThemeConfig: DarkThemeConfig,
  val useDynamicColor: Boolean,
  val playbackMode: PlaybackMode,
  val playingQueueIndex: Int,
  val playingQueueIds: List<String>,
)