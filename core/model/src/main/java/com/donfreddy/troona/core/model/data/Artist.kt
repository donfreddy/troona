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

import com.donfreddy.troona.core.model.util.ModelUtil

data class Artist(
  val id: Long,
  val albums: List<Album>,
  val isAlbumArtist: Boolean = false
) {
  val name: String
    get() {
      val name = if (isAlbumArtist) getAlbumArtistName()
      else getArtistName()
      return when {
        ModelUtil.isVariousArtists(name) -> VARIOUS_ARTISTS
        ModelUtil.isArtistNameUnknown(name) -> UNKNOWN_ARTIST
        else -> name ?: "-"
      }
    }

  val numberOfSongs: Int
    get() = albums.sumOf { it.numberOfSongs }


  val numberOfAlbums: Int
    get() = albums.size

  val songs: List<Song>
    get() = albums.flatMap { it.songs }

  private fun safeGetFirstAlbum(): Album {
    return albums.firstOrNull() ?: Album.empty
  }

  private fun getArtistName(): String {
    return safeGetFirstAlbum().safeGetFirstSong().artistName
  }

  private fun getAlbumArtistName(): String? {
    return safeGetFirstAlbum().safeGetFirstSong().albumArtist
  }

  companion object {
    const val UNKNOWN_ARTIST = "Unknown Artist"
    const val VARIOUS_ARTISTS = "Various Artists"
    val empty = Artist(-1, emptyList())
  }
}
