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

package com.donfreddy.troona.core.domain.usecase.settings

import com.donfreddy.troona.core.domain.repository.SettingsRepository
import com.donfreddy.troona.core.domain.usecase.UseCase
import com.donfreddy.troona.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case which returns the user data.
 */
class GetUserDataUseCase @Inject constructor(
  private val settingsRepository: SettingsRepository
) : UseCase<UserData, UseCase.NoParams> {
  operator fun invoke(): Flow<UserData> = settingsRepository.userData
}