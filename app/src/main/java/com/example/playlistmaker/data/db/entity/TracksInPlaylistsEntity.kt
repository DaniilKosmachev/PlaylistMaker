package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlists_table")
data class TracksInPlaylistsEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val trackId: Int,
    val playlistId: Int
)