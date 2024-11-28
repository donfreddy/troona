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

data class LastFmArtist(
  @Expose var artist: Artist = Artist()
) {
  data class Artist(
    @Expose var stats: Stats? = null,
    @Expose var bio: Bio? = null,
    @Expose var image: List<Image> = emptyList()
  ) {
    data class Image(
      @SerializedName("#text") @Expose var text: String? = null,

      @Expose var size: String? = null
    )

    data class Stats(
      @Expose var listeners: String? = null,

      @SerializedName("playcount") @Expose var playCount: String? = null
    )

    data class Bio(
      @Expose var content: String? = null,

      @Expose var published: String? = null
    )
  }
}