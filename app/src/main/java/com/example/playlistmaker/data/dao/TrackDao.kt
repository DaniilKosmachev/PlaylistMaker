package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorites_tracks_table ORDER BY track_add_time DESC")
    fun selectAllTracks(): List<TrackEntity>

    @Query("SELECT track_id FROM favorites_tracks_table")
    fun selectAllIdTracks(): List<Int>
}