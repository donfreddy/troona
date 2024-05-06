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

package com.donfreddy.troona.core.common.extensions

/**
 * Capitalize the first letter of each word in a string.
 */
fun String.capitalizeEachWord(ignoredWords: List<String> = emptyList()): String {
  val words = this.split(" ")
  //val capitalizedWords = words.map { replaceFirstChar { it.uppercase() } }
  val capitalizedWords = words.mapIndexed { index, word ->
    if (index == 0 || word !in ignoredWords) word.replaceFirstChar { it.uppercase() }
    else word.lowercase()
  }
  return capitalizedWords.joinToString(" ")
}