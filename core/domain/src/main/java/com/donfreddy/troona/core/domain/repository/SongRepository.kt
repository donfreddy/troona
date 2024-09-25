package com.donfreddy.troona.core.domain.repository

import com.donfreddy.troona.core.model.data.Album
import com.donfreddy.troona.core.model.data.Artist
import com.donfreddy.troona.core.model.data.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
  val songs: Flow<List<Song>>
  val artists: Flow<List<Artist>>
  val albums: Flow<List<Album>>
}