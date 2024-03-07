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
  }
}