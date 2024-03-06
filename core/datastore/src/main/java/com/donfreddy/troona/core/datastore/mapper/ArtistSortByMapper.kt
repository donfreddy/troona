/*
 * Copyright 2024 Don Freddy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.donfreddy.troona.core.datastore.mapper

import com.donfreddy.troona.core.datastore.ArtistSortByProto
import com.donfreddy.troona.core.model.enums.ArtistSortBy

internal fun ArtistSortBy.asArtistSortByProto() = when (this) {
  ArtistSortBy.Name -> ArtistSortByProto.ARTIST_SORT_BY_NAME
  ArtistSortBy.NumberOfSongs -> ArtistSortByProto.ARTIST_SORT_BY_NUMBER_OF_SONGS
  ArtistSortBy.NumberOfAlbums -> ArtistSortByProto.ARTIST_SORT_BY_NUMBER_OF_ALBUMS
}

internal fun ArtistSortByProto.asArtistSortBy() = when (this) {
  ArtistSortByProto.UNRECOGNIZED,
  ArtistSortByProto.ARTIST_SORT_BY_NAME -> ArtistSortBy.Name
  ArtistSortByProto.ARTIST_SORT_BY_NUMBER_OF_SONGS -> ArtistSortBy.NumberOfSongs
  ArtistSortByProto.ARTIST_SORT_BY_NUMBER_OF_ALBUMS -> ArtistSortBy.NumberOfAlbums
}
