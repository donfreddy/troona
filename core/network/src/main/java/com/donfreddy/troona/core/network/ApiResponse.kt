package com.donfreddy.troona.core.network

/**
 * Generic class that holds the network state
 */
sealed class ApiResponse<out R> {
  data class Success<out T>(val data: T) : ApiResponse<T>()
  data object Loading : ApiResponse<Nothing>()
  data class Error(val error: Exception) : ApiResponse<Nothing>()
}