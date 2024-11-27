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

package com.donfreddy.troona.feature.artist.navigation

import androidx.annotation.OptIn
import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.donfreddy.troona.feature.artist.ArtistScreen
import kotlinx.serialization.Serializable

private const val ARTIST_ID = "artistId"

@Serializable
data class ArtistRoute(val artistId: Long)

internal fun SavedStateHandle.getArtistId(): Long = checkNotNull(this[ARTIST_ID])

fun NavController.navigateToArtist(artistId: Long, navOptions: NavOptions) {
  navigate(ArtistRoute(artistId), navOptions)
}

@OptIn(UnstableApi::class)
fun NavGraphBuilder.artistScreen(
  onBackClick: () -> Unit,
  onAlbumClick: (albumId: Long) -> Unit,
) {
  composable<ArtistRoute> { backStackEntry ->
    val args = backStackEntry.toRoute<ArtistRoute>()
    SavedStateHandle()[ARTIST_ID] = args.artistId
    ArtistScreen(onBackClick = onBackClick, onAlbumClick = onAlbumClick)
  }
}