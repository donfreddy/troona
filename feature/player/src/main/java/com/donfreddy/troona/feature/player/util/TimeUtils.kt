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

package com.donfreddy.troona.feature.player.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.donfreddy.troona.feature.player.R
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

internal fun convertToProgress(count: Long, total: Long) =
  ((count * 100f) / total / 100f).takeIf(Float::isFinite) ?: 0f

internal fun convertToPosition(value: Float, total: Long) = (value * total).toLong()

@Composable
internal fun Long.asFormattedString() = milliseconds.toComponents { minutes, seconds, _ ->
  stringResource(
    id = R.string.feature_player_timestamp_format,
    String.format(locale = Locale.US, format = "%02d", minutes),
    String.format(locale = Locale.US, format = "%02d", seconds)
  )
}