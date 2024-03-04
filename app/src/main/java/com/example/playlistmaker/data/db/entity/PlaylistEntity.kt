package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlist_id")
    val id: Int?,
    @ColumnInfo(name = "playlist_name")
    val name: String,
    @ColumnInfo(name = "playlist_description")
    val description: String?,
    @ColumnInfo(name = "playlist_artwork_uri")
    val uri: String?,
    @ColumnInfo(name = "playlist_tracks_id")
    val tracksId: String?,
    @ColumnInfo(name = "playlist_tracks_count")
    val count: Int?
)
