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

package com.donfreddy.troona.core.media.util

import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.donfreddy.troona.core.model.data.Song

internal fun buildPlayableMediaItem(song: Song) = MediaItem.Builder()
  .setMediaId(song.id.toString())
  .setRequestMetadata(
    MediaItem.RequestMetadata.Builder()
      .setMediaUri(song.uri)
      .build()
  )
  .setMediaMetadata(
    MediaMetadata.Builder()
      .setArtworkUri(song.albumArt)
      .setTitle(song.title)
      .setArtist(song.artistName)
      .setIsBrowsable(false)
      .setIsPlayable(true)
      .setExtras(
        bundleOf(
          DISPLAY_NAME to song.displayName,
          ARTIST_ID to song.artistId,
          ALBUM_ID to song.albumId,
          DURATION to song.duration,
          SIZE to song.size,
          YEAR to song.year,
          MINE_TYPE to song.mineType,
          //DATA_ADDED to song.dateAdded,
          //DATA_MODIFIED to song.dateModified
        )
      )
      .build()
  )
  .build()

private const val DISPLAY_NAME = "display_name"
private const val ARTIST_ID = "artist_id"
private const val ALBUM_ID = "album_id"
private const val DURATION = "duration"
private const val SIZE = "size"
private const val YEAR = "year"
private const val MINE_TYPE = "mine_type"
private const val DATA_ADDED = "date_added"
private const val DATA_MODIFIED = "date_modified"
