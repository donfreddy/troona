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

package com.donfreddy.troona.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.donfreddy.troona.feature.album.navigation.albumScreen
import com.donfreddy.troona.feature.artist.navigation.artistScreen
import com.donfreddy.troona.feature.favorites.navigation.favoritesScreen
import com.donfreddy.troona.feature.home.navigation.HomeRoute
import com.donfreddy.troona.feature.home.navigation.homeScreen
import com.donfreddy.troona.feature.playlists.navigation.playlistsScreen
import com.donfreddy.troona.feature.settings.navigation.settingsScreen
import com.donfreddy.troona.ui.TroonaAppState

@Composable
fun TroonaNavHost(
  //navController: NavHostController,
  //onNavigateToArtist: (prefix: String, artistId: Long) -> Unit,
  /*  onSetSystemBarsLightIcons: () -> Unit,
    onResetSystemBarsIcons: () -> Unit,*/
  appState: TroonaAppState,
  modifier: Modifier = Modifier,
) {
  val navController = appState.navController
  NavHost(
    navController = navController,
    startDestination = HomeRoute,
    modifier = modifier,
  ) {
    homeScreen(
      onArtistClick = appState::navigateToArtist,
      onAlbumClick = appState::navigateToAlbum,
    )
    favoritesScreen()
    playlistsScreen()
    settingsScreen()

    albumScreen(
      onBackClick = appState::onBackClick,
      onAlbumClick = appState::navigateToAlbum,
    )
    artistScreen(
      onBackClick = appState::onBackClick,
      onAlbumClick = appState::navigateToAlbum,
    )
  }
}