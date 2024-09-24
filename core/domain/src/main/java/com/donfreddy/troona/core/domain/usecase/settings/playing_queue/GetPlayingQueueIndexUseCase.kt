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

package com.donfreddy.troona.core.domain.usecase.settings.playing_queue

import com.donfreddy.troona.core.domain.repository.SettingsRepository
import com.donfreddy.troona.core.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * A use case which return the playing queue index.
 */
class GetPlayingQueueIndexUseCase @Inject constructor(
  private val repository: SettingsRepository
) : UseCase<Flow<Int>, UseCase.NoParams> {
  operator fun invoke() = repository.userData.map { it.playingQueueIndex }
}
