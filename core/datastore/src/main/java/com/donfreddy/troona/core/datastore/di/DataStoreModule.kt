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

package com.donfreddy.troona.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.donfreddy.troona.UserPreferences
import com.donfreddy.troona.core.common.network.Dispatcher
import com.donfreddy.troona.core.common.network.TroonaDispatchers.IO
import com.donfreddy.troona.core.common.di.ApplicationScope
import com.donfreddy.troona.core.datastore.serializer.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

  @Provides
  @Singleton
  internal fun providesUserPreferencesDataStore(
    @ApplicationContext context: Context,
    @Dispatcher(IO) ioDispatcher: CoroutineDispatcher,
    @ApplicationScope scope: CoroutineScope,
    userPreferencesSerializer: UserPreferencesSerializer,
  ): DataStore<UserPreferences> = DataStoreFactory.create(
    serializer = userPreferencesSerializer,
    scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
  ) { context.dataStoreFile(USER_PREFERENCES_DATA_STORE_FILE) }
}

private const val USER_PREFERENCES_DATA_STORE_FILE = "user_preferences.pb"