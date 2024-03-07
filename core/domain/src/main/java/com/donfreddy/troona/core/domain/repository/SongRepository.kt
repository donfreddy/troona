package com.donfreddy.troona.core.domain.repository

import com.donfreddy.troona.core.model.data.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
  val songs: Flow<List<Song>>
  //val song: (id: Long) -> Flow<Song>
}