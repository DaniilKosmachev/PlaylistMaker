package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TracksInPlaylistsEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun createNewPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    fun selectAllPlaylists(): List<PlaylistEntity>

    @Insert(entity = TracksInPlaylistsEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun insertTrackInPlaylist(tracksInPlaylistsEntity: TracksInPlaylistsEntity)

    @Query("UPDATE playlists_table SET playlist_tracks_count = (SELECT  COUNT(*) from tracks_in_playlists_table WHERE playlistId = :playListId) WHERE playlist_id = :playListId")
    fun updateTrackCountInPlaylist(playListId: Int)

//    @Query("SELECT * FROM tracks_in_playlists_table WHERE trackId = :trackId and playlistId = :playlistId")
//    fun checkTrackInPlaylists(trackId: Int, playlistId: Int): TrackEntity?
}