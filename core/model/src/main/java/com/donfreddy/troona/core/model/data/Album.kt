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

data class Album(
  val id: Long,
  val songs: List<Song>
) {
  val title: String
    get() = safeGetFirstSong().albumName

  val artistId: Long
    get() = safeGetFirstSong().artistId

  val artistName: String
    get() = safeGetFirstSong().artistName

  val year: Int
    get() = safeGetFirstSong().year

  val dateModified: Long
    get() = safeGetFirstSong().dateModified

  val songCount: Int
    get() = songs.size

  val albumArtist: String?
    get() = safeGetFirstSong().albumArtist

  val numberOfSongs: Int
    get() = songs.size

  fun safeGetFirstSong(): Song {
    return songs.firstOrNull() ?: Song.emptySong
  }

  companion object {
    val empty = Album(-1, emptyList())
  }
}
