package com.example.playlistmaker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    @Transaction
    fun addTrackInPlaylistTransaction(
        tracksInPlaylistsEntity: TracksInPlaylistsEntity,
        playListId: Int
    ) {
        insertTrackInPlaylist(tracksInPlaylistsEntity)
        updateTrackCountInPlaylist(playListId)
    }

    @Query("SELECT playlistId FROM tracks_in_playlists_table WHERE trackId = :trackId")
    fun checkTrackInPlaylists(trackId: Int): List<Int>


    @Query("SELECT * FROM tracks_in_playlists_table WHERE playlistId = :playListId")
    fun selectAllTracksInPlaylist(playListId: Int): List<TracksInPlaylistsEntity>


    @Query("DELETE FROM tracks_in_playlists_table WHERE trackId = :trackId and playlistId = :playListId")
    fun removeTrackFromPlaylist(trackId: Int, playListId: Int)

    @Transaction
    fun deleteTrackFromDbTransaction(
        trackId: Int,
        playListId: Int
    ) {
        removeTrackFromPlaylist(trackId,playListId)
        updateTrackCountInPlaylist(playListId)
    }
}