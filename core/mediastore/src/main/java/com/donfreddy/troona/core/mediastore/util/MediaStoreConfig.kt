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

package com.donfreddy.troona.core.mediastore.util

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Genres

internal object MediaStoreConfig {
  object Song {
    val Collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    val Projection = arrayOf(
      MediaStore.Audio.Media._ID,
      MediaStore.Audio.Media.TITLE,
      MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.TRACK,
      MediaStore.Audio.Media.DURATION,
      MediaStore.Audio.Media.SIZE,
      MediaStore.Audio.Media.YEAR,
      MediaStore.Audio.Media.ALBUM_ID,
      MediaStore.Audio.Media.ALBUM,
      MediaStore.Audio.Media.ALBUM_ARTIST,
      MediaStore.Audio.Media.ARTIST_ID,
      MediaStore.Audio.Media.ARTIST,
      MediaStore.Audio.Media.DATA,
      MediaStore.Audio.Media.MIME_TYPE,
      MediaStore.Audio.Media.COMPOSER,
      MediaStore.Audio.Media.DATE_ADDED,
      MediaStore.Audio.Media.DATE_MODIFIED,
      MediaStore.Audio.Media.IS_ALARM,
      MediaStore.Audio.Media.IS_MUSIC,
      MediaStore.Audio.Media.IS_NOTIFICATION,
      MediaStore.Audio.Media.IS_PODCAST,
      MediaStore.Audio.Media.IS_RINGTONE,
      /*if (Build.VERSION.SDK_INT >= 29) MediaStore.Audio.Media.IS_AUDIOBOOK else null,
      if (Build.VERSION.SDK_INT >= 30) MediaStore.Audio.Media.GENRE else null,
      if (Build.VERSION.SDK_INT >= 30) MediaStore.Audio.Media.GENRE_ID else null,*/
    )

    val ArtistProjection = arrayOf(
      MediaStore.Audio.Artists._ID,
      MediaStore.Audio.Artists.ARTIST,
      MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
      MediaStore.Audio.Artists.NUMBER_OF_TRACKS
    )

    val ArtistAlbumProjection = arrayOf(
      MediaStore.Audio.Artists.Albums._ID,
      MediaStore.Audio.Artists.Albums.ARTIST,
      MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS,
      MediaStore.Audio.Artists.Albums.LAST_YEAR,
    )

    val AlbumProjection = arrayOf(
      MediaStore.Audio.Albums.ALBUM_ID,
      MediaStore.Audio.Albums.ALBUM,
      MediaStore.Audio.Albums.ARTIST,
      MediaStore.Audio.Albums.NUMBER_OF_SONGS,
      MediaStore.Audio.Albums.LAST_YEAR,
    )
  }

  object Genre {
    fun getCollection(genreId: Long): Uri {
      return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Genres.Members.getContentUri(MediaStore.VOLUME_EXTERNAL, genreId)
      } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
      }
    }

    val Projection = arrayOf(Genres._ID, Genres.NAME)
  }
}
