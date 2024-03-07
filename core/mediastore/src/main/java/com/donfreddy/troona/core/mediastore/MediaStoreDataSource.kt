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

package com.donfreddy.troona.core.mediastore

import android.content.ContentResolver
import android.provider.MediaStore
import com.donfreddy.troona.core.mediastore.extensions.asArtworkUri
import com.donfreddy.troona.core.mediastore.extensions.asLocalDateTime
import com.donfreddy.troona.core.mediastore.extensions.asUri
import com.donfreddy.troona.core.mediastore.extensions.getInt
import com.donfreddy.troona.core.mediastore.extensions.getLong
import com.donfreddy.troona.core.mediastore.extensions.getString
import com.donfreddy.troona.core.mediastore.extensions.observe
import com.donfreddy.troona.core.mediastore.util.MediaStoreConfig
import com.donfreddy.troona.core.model.data.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaStoreDataSource @Inject constructor(private val contentResolver: ContentResolver) {

  fun getSongs(
    selection: String? = null,
    sortOrder: String? = null,
    excludedFolders: List<String> = listOf("WhatsApp Audio")
  ): Flow<List<Song>> {
    return contentResolver.observe(uri = MediaStoreConfig.Song.Collection).map {
      val newSelection = StringBuilder().apply { append(selection ?: "") }
      newSelection.append("${MediaStore.Audio.Media.IS_MUSIC} = 1")
      excludedFolders.forEach { _ -> newSelection.append(" AND ${MediaStore.Audio.Media.DATA} NOT LIKE ?") }

      contentResolver.query(
        MediaStoreConfig.Song.Collection,
        MediaStoreConfig.Song.Projection,
        newSelection.toString(),
        excludedFolders.map { "%$it%" }.toTypedArray(),
        sortOrder
      )?.use { cursor ->
        List(cursor.count) {
          cursor.moveToNext()
          Song(
            id = cursor.getLong(MediaStore.Audio.Media._ID),
            title = cursor.getString(MediaStore.Audio.Media.TITLE),
            displayName = cursor.getString(MediaStore.Audio.Media.DISPLAY_NAME),
            trackNumber = cursor.getInt(MediaStore.Audio.Media.TRACK),
            duration = cursor.getLong(MediaStore.Audio.Media.DURATION),
            size = cursor.getInt(MediaStore.Audio.Media.SIZE),
            year = cursor.getInt(MediaStore.Audio.Media.YEAR),
            albumId = cursor.getLong(MediaStore.Audio.Media.ALBUM_ID),
            albumName = cursor.getString(MediaStore.Audio.Media.ALBUM),
            albumArt = cursor.getLong(MediaStore.Audio.Media.ALBUM_ID).asArtworkUri(),
            albumArtist = cursor.getString(MediaStore.Audio.Media.ALBUM_ARTIST),
            uri = cursor.getLong(MediaStore.Audio.Media._ID).asUri(),
            artistId = cursor.getLong(MediaStore.Audio.Media.ARTIST_ID),
            artistName = cursor.getString(MediaStore.Audio.Media.ARTIST),
            genreId = null,
            genreName = null,
            data = cursor.getString(MediaStore.Audio.Media.DATA),
            mineType = cursor.getString(MediaStore.Audio.Media.MIME_TYPE),
            composer = cursor.getString(MediaStore.Audio.Media.COMPOSER),
            dateAdded = cursor.getLong(MediaStore.Audio.Media.DATE_ADDED).asLocalDateTime(),
            dateModified = cursor.getLong(MediaStore.Audio.Media.DATE_MODIFIED).asLocalDateTime(),
          )
        }
      } ?: emptyList()
    }
  }
}
