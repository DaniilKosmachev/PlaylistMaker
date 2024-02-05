package com.example.playlistmaker.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorites_tracks_table")
    suspend fun selectAllTracks(): List<TrackEntity>

    @Query("SELECT track_id FROM favorites_tracks_table")
    suspend fun selectAllIdTracks(): List<Int>
}