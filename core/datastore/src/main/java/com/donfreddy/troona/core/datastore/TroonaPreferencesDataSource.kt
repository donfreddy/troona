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

package com.donfreddy.troona.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.donfreddy.troona.UserPreferences
import com.donfreddy.troona.copy
import com.donfreddy.troona.core.datastore.mapper.asAlbumSortBy
import com.donfreddy.troona.core.datastore.mapper.asAlbumSortByProto
import com.donfreddy.troona.core.datastore.mapper.asArtistSortBy
import com.donfreddy.troona.core.datastore.mapper.asArtistSortByProto
import com.donfreddy.troona.core.datastore.mapper.asDarkThemeConfig
import com.donfreddy.troona.core.datastore.mapper.asDarkThemeConfigProto
import com.donfreddy.troona.core.datastore.mapper.asGenreSortBy
import com.donfreddy.troona.core.datastore.mapper.asGenreSortByProto
import com.donfreddy.troona.core.datastore.mapper.asPlaybackMode
import com.donfreddy.troona.core.datastore.mapper.asPlaybackModeProto
import com.donfreddy.troona.core.datastore.mapper.asSongSortBy
import com.donfreddy.troona.core.datastore.mapper.asSongSortByProto
import com.donfreddy.troona.core.datastore.mapper.asSortBy
import com.donfreddy.troona.core.datastore.mapper.asSortByProto
import com.donfreddy.troona.core.model.data.UserData
import com.donfreddy.troona.core.model.enums.AlbumSortBy
import com.donfreddy.troona.core.model.enums.ArtistSortBy
import com.donfreddy.troona.core.model.enums.DarkThemeConfig
import com.donfreddy.troona.core.model.enums.GenreSortBy
import com.donfreddy.troona.core.model.enums.PlaybackMode
import com.donfreddy.troona.core.model.enums.SongSortBy
import com.donfreddy.troona.core.model.enums.SortOrder
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class TroonaPreferencesDataSource @Inject constructor(
  private val userPreferences: DataStore<UserPreferences>,
) {
  val userData = userPreferences.data.map {
    UserData(
      songSortOrder = it.songSortOrder.asSortBy(),
      songSortBy = it.songSortBy.asSongSortBy(),
      artistSortBy = it.artistSortBy.asArtistSortBy(),
      artistSortOrder = it.artistSortOrder.asSortBy(),
      albumSortBy = it.albumSortBy.asAlbumSortBy(),
      albumSortOrder = it.albumSortOrder.asSortBy(),
      genreSortBy = it.genreSortBy.asGenreSortBy(),
      genreSortOrder = it.genreSortOrder.asSortBy(),
      ignoreCase = it.ignoreCase,
      darkThemeConfig = it.darkThemeConfig.asDarkThemeConfig(),
      useDynamicColor = it.useDynamicColor,
      playbackMode = it.playbackMode.asPlaybackMode(),
      playingQueueIndex = it.playingQueueIndex,
      playingQueueIds = it.playingQueueIdsList,
    )
  }

  suspend fun setSongSortBy(songSortBy: SongSortBy) {
    userPreferences.updateData {
      it.copy { this.songSortBy = songSortBy.asSongSortByProto() }
    }
  }

  suspend fun setSongSortOrder(sortOrder: SortOrder) {
    userPreferences.updateData {
      it.copy { this.songSortOrder = sortOrder.asSortByProto() }
    }
  }

  suspend fun setArtistSortBy(artistSortBy: ArtistSortBy) {
    userPreferences.updateData {
      it.copy { this.artistSortBy = artistSortBy.asArtistSortByProto() }
    }
  }

  suspend fun setArtistSortOrder(sortOrder: SortOrder) {
    userPreferences.updateData {
      it.copy { this.artistSortOrder = sortOrder.asSortByProto() }
    }
  }

  suspend fun setAlbumSortBy(albumSortBy: AlbumSortBy) {
    userPreferences.updateData {
      it.copy { this.albumSortBy = albumSortBy.asAlbumSortByProto() }
    }
  }

  suspend fun setAlbumSortOrder(sortOrder: SortOrder) {
    userPreferences.updateData {
      it.copy { this.albumSortOrder = sortOrder.asSortByProto() }
    }
  }

  suspend fun setGenreSortBy(genreSortBy: GenreSortBy) {
    userPreferences.updateData {
      it.copy { this.genreSortBy = genreSortBy.asGenreSortByProto() }
    }
  }

  suspend fun setGenreSortOrder(sortOrder: SortOrder) {
    userPreferences.updateData {
      it.copy { this.genreSortOrder = sortOrder.asSortByProto() }
    }
  }

  suspend fun setIgnoreCase(ignoreCase: Boolean) {
    userPreferences.updateData {
      it.copy { this.ignoreCase = ignoreCase }
    }
  }

  suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
    userPreferences.updateData {
      it.copy { this.darkThemeConfig = darkThemeConfig.asDarkThemeConfigProto() }
    }
  }

  suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
    userPreferences.updateData {
      it.copy { this.useDynamicColor = useDynamicColor }
    }
  }

  suspend fun setPlaybackMode(playbackMode: PlaybackMode) {
    userPreferences.updateData {
      it.copy { this.playbackMode = playbackMode.asPlaybackModeProto() }
    }
  }

  suspend fun setPlayingQueueIndex(playingQueueIndex: Int) {
    userPreferences.updateData {
      it.copy { this.playingQueueIndex = playingQueueIndex }
    }
  }

  suspend fun setPlayingQueueIds(playingQueueIds: List<String>) {
    try {
      userPreferences.updateData {
        it.copy {
          this.playingQueueIds.clear()
          this.playingQueueIds.addAll(playingQueueIds)
        }
      }
    } catch (ioException: IOException) {
      Log.e(TAG, ERROR_MESSAGE, ioException)
    }
  }
}

private const val TAG = "TroonaPreferences"
private const val ERROR_MESSAGE = "Failed to update user preferences"