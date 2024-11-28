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

package com.donfreddy.troona.core.network

import com.donfreddy.troona.core.network.models.LastFmAlbum
import com.donfreddy.troona.core.network.models.LastFmArtist
import timber.log.Timber
import javax.inject.Inject

interface NetworkRepository {
  suspend fun artistInfo(name: String, lang: String?, cache: String?): ApiResponse<LastFmArtist>
  suspend fun albumInfo(artist: String, lang: String?, album: String): ApiResponse<LastFmAlbum>
}

class NetworkRepositoryImpl @Inject constructor(
  private val lastFMService: LastFMService
) : NetworkRepository {

  override suspend fun artistInfo(
    name: String,
    lang: String?,
    cache: String?
  ): ApiResponse<LastFmArtist> {
    return try {
      ApiResponse.Success(lastFMService.artistInfo(name, lang, cache))
    } catch (e: Exception) {
      Timber.e(name, e.message ?: "Error fetching artist info")
      ApiResponse.Error(e)
    }
  }

  override suspend fun albumInfo(
    artist: String,
    lang: String?,
    album: String
  ): ApiResponse<LastFmAlbum> {
    return try {
      val lastFmAlbum = lastFMService.albumInfo(artist, lang, album)
      ApiResponse.Success(lastFmAlbum)
    } catch (e: Exception) {
      Timber.e(artist, e.message ?: "Error fetching album info")
      ApiResponse.Error(e)
    }
  }
}