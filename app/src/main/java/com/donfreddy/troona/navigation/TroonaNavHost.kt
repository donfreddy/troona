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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.donfreddy.troona.feature.home.navigation.HOME_ROUTE
import com.donfreddy.troona.feature.home.navigation.homeScreen
import com.donfreddy.troona.feature.favorites.navigation.favoritesScreen
import com.donfreddy.troona.feature.playlists.navigation.playlistsScreen
import com.donfreddy.troona.feature.settings.navigation.settingsScreen

@Composable
fun TroonaNavHost(
  navController: NavHostController,
  onNavigateToPlayer: () -> Unit,
  modifier: Modifier = Modifier,
  startDestination: String = HOME_ROUTE
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier,
  ) {
    homeScreen(
      onNavigateToPlayer = onNavigateToPlayer,
      //onNavigateToArtist = navController::navigateToArtist,
      //onNavigateToAlbum = navController::navigateToAlbum,
    )
    favoritesScreen()
    playlistsScreen()
    settingsScreen()
  }
}