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

package com.donfreddy.troona.core.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LastFmAlbum(
  @Expose private var album: Album = Album()
) {
  fun getAlbum() = album
  fun setAlbum(album: Album) {
    this.album = album
  }

  data class Album(
    @Expose var name: String? = null,
    @Expose var listeners: String? = null,
    @SerializedName("playcount") @Expose var playCount: String? = null,
    @Expose var wiki: Wiki? = null,
    @Expose var image: List<Image> = emptyList()
  ) {
    data class Image(
      @SerializedName("#text") @Expose var text: String? = null,

      @Expose var size: String? = null
    )

    data class Wiki(
      @Expose var content: String? = null,

      @Expose val published: String? = null
    )
  }
}