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

package com.donfreddy.troona.core.network.di

import com.donfreddy.troona.core.network.LastFMService
import com.donfreddy.troona.core.network.NetworkRepository
import com.donfreddy.troona.core.network.NetworkRepositoryImpl
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .addNetworkInterceptor(
        HttpLoggingInterceptor().apply {
          // Todo: Change to NONE in production
          level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        }
      )
      .addInterceptor(Interceptor {
        val original = it.request()
        val request = original.newBuilder()
          //.header("User-Agent", context.packageName)
          .addHeader("Content-Type", "application/json; charset=utf-8")
          .method(original.method, original.body)
          .build()
        it.proceed(request)
      })
      .readTimeout(2, TimeUnit.SECONDS)
      .connectTimeout(2, TimeUnit.SECONDS)
      .cache(null)
      .build()
  }

  @Provides
  @Singleton
  fun provideLastFmRetrofit(client: OkHttpClient): Retrofit {
    val gson = GsonBuilder()
      .setLenient()
      .create()
    return Retrofit.Builder()
      .baseUrl("https://ws.audioscrobbler.com/2.0/")
      .addConverterFactory(GsonConverterFactory.create(gson))
      .callFactory { request -> client.newCall(request) }
      .build()
  }

  @Provides
  @Singleton
  fun provideLastFmRest(retrofit: Retrofit): LastFMService {
    return retrofit.create(LastFMService::class.java)
  }

  @Provides
  @Singleton
  fun provideNetworkRepository(lastFMService: LastFMService): NetworkRepository {
    return NetworkRepositoryImpl(lastFMService)
  }
}