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

package com.donfreddy.troona.core.domain.usecase

/**
 * This abstract class is a base class for all use cases.
 * It provides a way to execute use cases with parameters or without parameters.
 * @param [T] the type of the use case result.
 * @param [Params] the type of the use case parameters.
 * @author Don Freddy
 */
abstract class UseCase<out T, in Params> {

  open class NoParams
}