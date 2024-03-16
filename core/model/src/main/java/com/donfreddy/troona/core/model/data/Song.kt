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

import android.net.Uri
import kotlinx.datetime.LocalDateTime

data class Song(
  val id: Long,
  val title: String,
  val displayName: String,
  val trackNumber: Int,
  val duration: Long,
  val size: Int,
  val year: Int?,
  val albumId: Long,
  val albumName: String,
  val albumArt: Uri,
  val albumArtist: String?,
  val artistId: Long,
  val artistName: String,
  val genreId: Long?, // Only Api >= 30/Android 11
  val genreName: String?, // Only Api >= 30/Android 11
  val data: String?,
  val uri: Uri,
  //val folder: String,
  val mineType: String?,
  val composer: String?,
  val dateAdded: LocalDateTime?,
  val dateModified: LocalDateTime?,
) {
  companion object {
    val EMPTY = Song(
      id = -1,
      title = "Unknown",
      displayName = "Unknown",
      trackNumber = -1,
      duration = -1,
      size = -1,
      year = -1,
      albumId = -1,
      albumName = "Unknown",
      albumArt = Uri.EMPTY,
      albumArtist = "Unknown",
      artistId = -1,
      artistName = "Unknown",
      genreId = -1,
      genreName = "",
      data = "",
      uri = Uri.EMPTY,
      // folder = "",
      mineType = "",
      composer = "Unknown",
      dateAdded = null,
      dateModified = null,
    )

    val EXAMPLE = Song(
      id = 416,
      title = "2 Lit 2 Late Interlude",
      displayName = "14 - Nicki Minaj -  2 Lit 2 Late Interlude (Explicit).mp3",
      trackNumber = 1014,
      duration = 55301,
      size = 2325989,
      year = 2018,
      albumId = 66,
      albumName = "Queen (Deluxe)",
      albumArt = Uri.parse("content://media/external/audio/albumart/66"),
      albumArtist = "Nicki Minaj",
      artistId = 69,
      artistName = "Nicki Minaj",
      genreId = null,
      genreName = null,
      data = "/storage/5BA9-364A/Collection/Nicki Minaj/nicki-minaj---queen-deluxe-explicit-2018/14 - Nicki Minaj -  2 Lit 2 Late Interlude (Explicit).mp3",
      uri = Uri.parse("content://media/external/audio/media/416"),
      // folder = "",
      mineType = "audio/mpeg",
      composer = null,
      dateAdded = LocalDateTime.parse("2022-06-13T20:45:44"),
      dateModified = LocalDateTime.parse("2022-03-17T15:35:18")
    )
  }
}