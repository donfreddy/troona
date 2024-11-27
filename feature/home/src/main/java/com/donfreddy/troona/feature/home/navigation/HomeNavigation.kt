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

package com.donfreddy.troona.feature.home.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.donfreddy.troona.feature.home.HomeScreen
import kotlinx.serialization.Serializable


@Serializable
data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HomeRoute, navOptions)

fun NavGraphBuilder.homeScreen(
  onArtistClick: (artistId: Long) -> Unit,
  onAlbumClick: (albumId: Long) -> Unit,
) {
  // To animate the transition between screens, we use the AnimatedContent composable
  // See: https://tomasrepcik.dev/blog/2023/2023-10-29-android-compose-animations
  composable<HomeRoute> {
    HomeScreen(onArtistClick, onAlbumClick)
  }
}
