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

package com.donfreddy.troona.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
  tableName = "songs",
  indices = [Index(value = ["playlist_id", "id"], unique = true)]
)
data class SongEntity(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "song_key")
  val songKey: Long,

  @ColumnInfo(name = "playlist_id") val playlistId: Int,

  @ColumnInfo val id: Long,

  @ColumnInfo val title: String,

  @ColumnInfo val year: Int,

  @ColumnInfo val duration: Long,

  @ColumnInfo val data: String,

  @ColumnInfo val size: Int,

  @ColumnInfo val folder: String?,

  @ColumnInfo val composer: String?,

  @ColumnInfo(name = "track_number") val trackNumber: Int,

  @ColumnInfo("album_id") val albumId: Long,

  @ColumnInfo("album_name") val albumName: String,

  @ColumnInfo(name = "album_artist") val albumArtist: String?,

  @ColumnInfo(name = "artist_id") val artistId: Long,

  @ColumnInfo(name = "artist_name") val artistName: String,

  @ColumnInfo("date_added") val dateAdded: LocalDate,

  @ColumnInfo("date_modified") val dateModified: LocalDate,
)