package com.donfreddy.troona.core.data.util

object DataUtil {
  fun getOrderDetails(order: Int, ignoreCase: Boolean): Pair<String, String> {
    val sortOrder = if (order == 0) "ASC" else "DESC"
    val orderAndCase = if (ignoreCase) "$sortOrder COLLATE NOCASE" else sortOrder
    return Pair(sortOrder, orderAndCase)
  }
}