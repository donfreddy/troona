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

package com.donfreddy.troona.core.designsystem.images

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons

/**
 * Todo: write something
 */
@Composable
fun TroonaImage(
  model: Any?,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  shape: Shape = MaterialTheme.shapes.medium,
  backgroundColor: Color = MaterialTheme.colors.surface,
  loading: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit)? = null,
  error: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
  contentScale: ContentScale = ContentScale.Crop
) {
  Card(
    modifier = modifier,
    shape = shape,
    elevation = 0.dp,
    backgroundColor = backgroundColor
  ) {
    SubcomposeAsyncImage(
      modifier = modifier.fillMaxSize(),
      model = model,
      contentDescription = contentDescription,
      loading = loading,
      error = error,
      contentScale = contentScale
    )
  }
}

@Composable
fun TroonaArtwork(
  artworkUri: Uri,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  shape: Shape = MaterialTheme.shapes.medium,
  backgroundColor: Color = Color.Transparent,
  placeholder: @Composable () -> Unit = {
    Card(
      shape = shape,
      elevation = 0.dp,
      contentColor = MaterialTheme.colors.primary.copy(alpha = 0.2f),
      backgroundColor = Color.White
    ) {
      Icon(
        painter = painterResource(id = TroonaIcons.Music.resourceId),
        modifier = Modifier.fillMaxSize(),
        contentDescription = contentDescription
      )
    }
  },
  contentScale: ContentScale = ContentScale.Crop
) {
  TroonaImage(
    modifier = modifier,
    model = artworkUri,
    contentDescription = contentDescription,
    shape = shape,
    backgroundColor = backgroundColor,
    loading = { placeholder() },
    error = { placeholder() },
    contentScale = contentScale
  )
}