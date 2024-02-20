/*
 * Copyright 2024 Don Freddy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.donfreddy.troona.core.datastore.mapper

import com.donfreddy.troona.core.datastore.SongSortByProto
import com.donfreddy.troona.core.model.enums.SongSortBy

internal fun SongSortBy.asSongSortByProto() = when (this) {
  SongSortBy.Title -> SongSortByProto.SONG_SORT_BY_TITLE
  SongSortBy.Artist -> SongSortByProto.SONG_SORT_BY_ARTIST
  SongSortBy.Album -> SongSortByProto.SONG_SORT_BY_ALBUM
  SongSortBy.Duration -> SongSortByProto.SONG_SORT_BY_DURATION
  SongSortBy.DateAdded -> SongSortByProto.SONG_SORT_BY_DATE_ADDED
  SongSortBy.Size -> SongSortByProto.SONG_SORT_BY_SIZE
}

internal fun SongSortByProto.asSongSortBy() = when (this) {
  SongSortByProto.UNRECOGNIZED,
  SongSortByProto.SONG_SORT_BY_TITLE -> SongSortBy.Title
  SongSortByProto.SONG_SORT_BY_ARTIST -> SongSortBy.Artist
  SongSortByProto.SONG_SORT_BY_ALBUM -> SongSortBy.Album
  SongSortByProto.SONG_SORT_BY_DURATION -> SongSortBy.Duration
  SongSortByProto.SONG_SORT_BY_DATE_ADDED -> SongSortBy.DateAdded
  SongSortByProto.SONG_SORT_BY_SIZE -> SongSortBy.Size
}
