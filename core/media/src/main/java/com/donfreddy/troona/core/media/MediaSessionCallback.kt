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

package com.donfreddy.troona.core.media

import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.donfreddy.troona.core.common.network.Dispatcher
import com.donfreddy.troona.core.common.network.TroonaDispatchers.Main
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@UnstableApi
class MediaSessionCallback @Inject constructor(
  // private val audioService: AudioService,
  @Dispatcher(Main) mainDispatcher: CoroutineDispatcher
) : MediaLibraryService.MediaLibrarySession.Callback {
  private val coroutineScope = CoroutineScope(mainDispatcher + SupervisorJob())
}