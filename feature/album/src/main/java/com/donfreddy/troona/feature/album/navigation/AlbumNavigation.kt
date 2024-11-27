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

package com.donfreddy.troona.feature.album.navigation

import androidx.annotation.OptIn
import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.donfreddy.troona.feature.album.AlbumScreen
import kotlinx.serialization.Serializable

private const val ALBUM_ID = "albumId"

@Serializable
data class AlbumRoute(val albumId: Long)

internal fun SavedStateHandle.getAlbumId(): Long = checkNotNull(this[ALBUM_ID])

fun NavController.navigateToAlbum(albumId: Long, navOptions: NavOptions) {
  navigate(AlbumRoute(albumId), navOptions)
}

@OptIn(UnstableApi::class)
fun NavGraphBuilder.albumScreen(
  onBackClick: () -> Unit,
  onAlbumClick: (albumId: Long) -> Unit,
) {
  composable<AlbumRoute> { backStackEntry ->
    val args = backStackEntry.toRoute<AlbumRoute>()
    SavedStateHandle()[ALBUM_ID] = args.albumId
    AlbumScreen(onBackClick = onBackClick, onAlbumClick = onAlbumClick)
  }
}
