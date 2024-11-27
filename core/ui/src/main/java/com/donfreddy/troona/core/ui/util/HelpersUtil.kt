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

package com.donfreddy.troona.core.ui.util

import android.content.Context
import com.donfreddy.troona.core.model.data.Artist
import com.donfreddy.troona.core.common.extensions.toDurationString
import com.donfreddy.troona.core.model.data.Album
import com.donfreddy.troona.core.ui.R

object HelpersUtil {

  fun getArtistInfoString(context: Context, artist: Artist): String {
    val totalAlbums = context.resources.getQuantityString(
      R.plurals.core_ui_number_of_albums, artist.numberOfAlbums, artist.numberOfAlbums
    )
    val totalSongs = context.resources.getQuantityString(
      R.plurals.core_ui_number_of_songs, artist.numberOfSongs, artist.numberOfSongs
    )
    return "$totalAlbums • $totalSongs" // 1 album • 10 songs
  }

  fun getArtistInfoStringWithDuration(context: Context, artist: Artist): String {
    val totalAlbums = context.resources.getQuantityString(
      R.plurals.core_ui_number_of_albums, artist.numberOfAlbums, artist.numberOfAlbums
    )
    val totalSongs = context.resources.getQuantityString(
      R.plurals.core_ui_number_of_songs, artist.numberOfSongs, artist.numberOfSongs
    )
    val totalDuration = artist.songs.map { it.duration }.toDurationString()
    return "$totalAlbums • $totalSongs • $totalDuration" // 1 album • 10 songs • 1h 30m
  }

  fun getAlbumInfoStringWithDuration(context: Context, album: Album): String {
    val albumYear = album.year.toString()
    val totalSongs = context.resources.getQuantityString(
      R.plurals.core_ui_number_of_songs, album.numberOfSongs, album.numberOfSongs
    )
    val totalDuration = album.songs.map { it.duration }.toDurationString()
    return "$albumYear • $totalSongs • $totalDuration" // 2021 • 10 songs • 1h 30m
  }

  fun getTotalAlbumsString(context: Context, numberOfAlbums: Int): String {
    return context.resources.getQuantityString(
      R.plurals.core_ui_number_of_albums, numberOfAlbums, numberOfAlbums
    )
  }

  fun getTotalSongsString(context: Context, numberOfSongs: Int): String {
    return context.resources.getQuantityString(
      R.plurals.core_ui_number_of_songs, numberOfSongs, numberOfSongs
    )
  }
}