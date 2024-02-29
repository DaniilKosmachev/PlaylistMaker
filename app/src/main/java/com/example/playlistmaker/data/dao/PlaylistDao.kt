package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun createNewPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    fun selectAllPlaylists(): List<PlaylistEntity>
}