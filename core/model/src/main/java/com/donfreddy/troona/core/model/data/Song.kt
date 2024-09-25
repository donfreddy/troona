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
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Song(
  val id: Long,
  val title: String,
  val displayName: String,
  val trackNumber: Int,
  val duration: Long,
  val size: Int,
  val year: Int,
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
  val dateAdded: Long,
  val dateModified: Long,
) : Parcelable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Song

    return listOf(
      id to other.id,
      title to other.title,
      displayName to other.displayName,
      trackNumber to other.trackNumber,
      duration to other.duration,
      size to other.size,
      year to other.year,
      albumId to other.albumId,
      albumName to other.albumName,
      albumArt to other.albumArt,
      albumArtist to other.albumArtist,
      artistId to other.artistId,
      artistName to other.artistName,
      genreId to other.genreId,
      genreName to other.genreName,
      data to other.data,
      uri to other.uri,
      mineType to other.mineType,
      composer to other.composer,
      dateAdded to other.dateAdded,
      dateModified to other.dateModified
    ).all { it.first == it.second }
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + title.hashCode()
    result = 31 * result + displayName.hashCode()
    result = 31 * result + trackNumber
    result = 31 * result + duration.hashCode()
    result = 31 * result + size
    result = 31 * result + year
    result = 31 * result + albumId.hashCode()
    result = 31 * result + albumName.hashCode()
    result = 31 * result + albumArt.hashCode()
    result = 31 * result + (albumArtist?.hashCode() ?: 0)
    result = 31 * result + artistId.hashCode()
    result = 31 * result + artistName.hashCode()
    result = 31 * result + (genreId?.hashCode() ?: 0)
    result = 31 * result + (genreName?.hashCode() ?: 0)
    result = 31 * result + (data?.hashCode() ?: 0)
    result = 31 * result + uri.hashCode()
    result = 31 * result + (mineType?.hashCode() ?: 0)
    result = 31 * result + (composer?.hashCode() ?: 0)
    result = 31 * result + dateAdded.hashCode()
    result = 31 * result + dateModified.hashCode()
    return result
  }


  companion object {
    @JvmStatic
    val emptySong = Song(
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
      composer = "",
      dateAdded = -1,
      dateModified = -1,
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
      dateAdded = 1615999518,
      dateModified = 1534262400,
    )
  }
}