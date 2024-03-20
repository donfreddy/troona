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

package com.donfreddy.troona.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.donfreddy.troona.core.designsystem.R
import com.donfreddy.troona.core.designsystem.icon.Icon.ImageVectorIcon
import com.donfreddy.troona.core.designsystem.icon.Icon.DrawableResourceIcon

object TroonaIcons {
  val Home = DrawableResourceIcon(R.drawable.ic_home)
  val HomeBorder = DrawableResourceIcon(R.drawable.ic_outline_home)
  val Favorite = DrawableResourceIcon(R.drawable.ic_favorite)
  val FavoriteBorder = DrawableResourceIcon(R.drawable.ic_outline_favorite)
  val Settings = DrawableResourceIcon(R.drawable.ic_settings)
  val SettingsBorder = DrawableResourceIcon(R.drawable.ic_outline_settings)

  // Single
  val Search = ImageVectorIcon(Icons.Rounded.Search)
  val Star = ImageVectorIcon(Icons.Rounded.Star)
  val Music = DrawableResourceIcon(R.drawable.ic_music)
  val Playlist = DrawableResourceIcon(R.drawable.ic_playlist)
  val ArrowBack = ImageVectorIcon(Icons.AutoMirrored.Rounded.ArrowBack)
  val Clear = ImageVectorIcon(Icons.Rounded.Clear)
  val Close = ImageVectorIcon(Icons.Rounded.Close)
  val Repeat = DrawableResourceIcon(R.drawable.ic_repeat)
  val Title = DrawableResourceIcon(R.drawable.ic_title)
  val MoreHorizontal = DrawableResourceIcon(R.drawable.ic_more_horiz)
  val MoreVert = DrawableResourceIcon(R.drawable.ic_more_vert)
  val RepeatOne = DrawableResourceIcon(R.drawable.ic_repeat_one)
  val Shuffle = DrawableResourceIcon(R.drawable.ic_shuffle)
  val SkipPrevious = DrawableResourceIcon(R.drawable.ic_skip_previous)
  val Play = DrawableResourceIcon(R.drawable.ic_play)
  val Pause = DrawableResourceIcon(R.drawable.ic_pause)
  val SkipNext = DrawableResourceIcon(R.drawable.ic_skip_next)
  val FastForward = DrawableResourceIcon(R.drawable.ic_fast_forward)
  val Sort = DrawableResourceIcon(R.drawable.ic_sort)
  val Palette = DrawableResourceIcon(R.drawable.ic_palette)
  val DarkMode = DrawableResourceIcon(R.drawable.ic_dark_mode)
  val GitHub = DrawableResourceIcon(R.drawable.ic_github)
  val Info = ImageVectorIcon(Icons.Rounded.Info)
  val Security = DrawableResourceIcon(R.drawable.ic_security)
}

sealed interface Icon {
  data class ImageVectorIcon(val imageVector: ImageVector) : Icon
  data class DrawableResourceIcon(@DrawableRes val resourceId: Int) : Icon
}