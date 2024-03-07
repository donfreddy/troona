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

import com.donfreddy.troona.core.data.util.sort_types.checkSongSortType
import com.donfreddy.troona.core.datastore.TroonaPrefsDataSource
import com.donfreddy.troona.core.domain.repository.SongRepository
import com.donfreddy.troona.core.mediastore.MediaStoreDataSource
import com.donfreddy.troona.core.model.data.Song
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
  mediaStoreDataSource: MediaStoreDataSource,
  prefsDataSource: TroonaPrefsDataSource
) : SongRepository {
  @OptIn(ExperimentalCoroutinesApi::class)
  override val songs: Flow<List<Song>> = prefsDataSource.userData
    .flatMapLatest { userData ->
      mediaStoreDataSource.getSongs(
        null,
        sortOrder = checkSongSortType(
          userData.songSortOrder.ordinal,
          userData.songSortBy.ordinal,
          userData.ignoreCase
        ),
        excludedFolders = excludedFolders
      )
    }

  //override val song: (id: Long) -> Flow<Song> = { id -> Unit}

  private val excludedFolders = listOf("Whatsapp Audio", "Whatsapp Business Audio")
}