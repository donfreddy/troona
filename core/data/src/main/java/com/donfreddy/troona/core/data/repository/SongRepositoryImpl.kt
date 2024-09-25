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

package com.donfreddy.troona.core.data.repository

import android.provider.MediaStore.Audio.ArtistColumns.ARTIST
import android.provider.MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS
import android.provider.MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS
import android.provider.MediaStore.Audio.AlbumColumns.ALBUM
import com.donfreddy.troona.core.data.util.sort_types.checkAlbumSortType
import com.donfreddy.troona.core.data.util.sort_types.checkArtistSortType
import com.donfreddy.troona.core.data.util.sort_types.checkSongSortType
import com.donfreddy.troona.core.datastore.TroonaPrefsDataSource
import com.donfreddy.troona.core.domain.repository.SongRepository
import com.donfreddy.troona.core.mediastore.MediaStoreDataSource
import com.donfreddy.troona.core.model.data.Album
import com.donfreddy.troona.core.model.data.Artist
import com.donfreddy.troona.core.model.data.Song
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.text.Collator
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
  mediaStoreDataSource: MediaStoreDataSource, prefsDataSource: TroonaPrefsDataSource
) : SongRepository {
  @OptIn(ExperimentalCoroutinesApi::class)
  override val songs: Flow<List<Song>> = prefsDataSource.userData.flatMapLatest { userData ->
    mediaStoreDataSource.getSongs(
      null, sortOrder = checkSongSortType(
        userData.songSortOrder.ordinal, userData.songSortBy.ordinal, userData.ignoreCase
      ), excludedFolders = excludedFolders
    )
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override val artists: Flow<List<Artist>> = prefsDataSource.userData.flatMapLatest { userData ->
    albums.map { albums ->
      albums.groupBy(Album::artistId).map { (artistId, albums) ->
        Artist(
          id = artistId,
          albums = albums,
          isAlbumArtist = !albums.first().albumArtist.isNullOrEmpty(),
        )
      }
    }.map { artists ->
      val collator = Collator.getInstance()
      val sortOrder = checkArtistSortType(
        userData.artistSortOrder.ordinal, userData.artistSortBy.ordinal, false
      )

      artists.sortedWith(when (sortOrder) {
        ARTIST_SORT_ORDER_ASC -> { a1, a2 ->
          collator.compare(a1.name, a2.name)
        }

        ARTIST_SORT_ORDER_DESC -> { a1, a2 ->
          collator.compare(a2.name, a1.name)
        }

        NUMBER_OF_SONGS_SORT_ORDER_ASC -> { a1, a2 ->
          collator.compare(a1.numberOfSongs, a2.numberOfSongs)
        }

        NUMBER_OF_SONGS_SORT_ORDER_DESC -> { a1, a2 ->
          collator.compare(a2.numberOfSongs, a1.numberOfSongs)
        }

        NUMBER_OF_ALBUMS_SORT_ORDER_ASC -> { a1, a2 ->
          collator.compare(a1.numberOfAlbums, a2.numberOfAlbums)
        }

        NUMBER_OF_ALBUMS_SORT_ORDER_DESC -> { a1, a2 ->
          collator.compare(a2.numberOfAlbums, a1.numberOfAlbums)
        }

        else -> throw IllegalArgumentException("Invalid sort order")
      })
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override val albums: Flow<List<Album>> = prefsDataSource.userData.flatMapLatest { userData ->
    songs.map { songs ->
      songs.groupBy(Song::albumId).map { (albumId, songs) ->
        Album(id = albumId, songs = songs)
      }
    }.map { albums ->
      val collator = Collator.getInstance()
      val sortOrder = checkAlbumSortType(
        userData.albumSortOrder.ordinal, userData.albumSortBy.ordinal, false
      )

      albums.sortedWith(when (sortOrder) {
        ALBUM_SORT_ORDER_ASC -> { a1, a2 ->
          collator.compare(a1.title, a2.title)
        }

        ALBUM_SORT_ORDER_DESC -> { a1, a2 ->
          collator.compare(a2.title, a1.title)
        }

        ARTIST_SORT_ORDER_ASC -> { a1, a2 ->
          collator.compare(a1.albumArtist, a2.artistName)
        }

        ARTIST_SORT_ORDER_DESC -> { a1, a2 ->
          collator.compare(a2.artistName, a1.artistName)
        }

        NUMBER_OF_SONGS_SORT_ORDER_ASC -> { a1, a2 ->
          collator.compare(a1.numberOfSongs, a2.numberOfSongs)
        }

        NUMBER_OF_SONGS_SORT_ORDER_DESC -> { a1, a2 ->
          collator.compare(a2.numberOfSongs, a1.numberOfSongs)
        }

        else -> throw IllegalArgumentException("Invalid sort order")
      })
    }
  }

  companion object {
    const val ALBUM_SORT_ORDER_ASC = "$ALBUM ASC"
    const val ALBUM_SORT_ORDER_DESC = "$ALBUM DESC"
    const val ARTIST_SORT_ORDER_ASC = "$ARTIST ASC"
    const val ARTIST_SORT_ORDER_DESC = "$ARTIST DESC"
    const val NUMBER_OF_SONGS_SORT_ORDER_ASC = "$NUMBER_OF_TRACKS ASC"
    const val NUMBER_OF_SONGS_SORT_ORDER_DESC = "$NUMBER_OF_TRACKS DESC"
    const val NUMBER_OF_ALBUMS_SORT_ORDER_ASC = "$NUMBER_OF_ALBUMS ASC"
    const val NUMBER_OF_ALBUMS_SORT_ORDER_DESC = "$NUMBER_OF_ALBUMS DESC"
    private val excludedFolders = listOf("Whatsapp Audio", "Whatsapp Business Audio")
  }
}