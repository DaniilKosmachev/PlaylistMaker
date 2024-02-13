package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_tracks_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "track_id")
    val id: Int,
    @ColumnInfo(name = "track_artwork_url")
    val artworkUrl100: String,
    @ColumnInfo(name = "track_name")
    val trackName: String,
    @ColumnInfo(name = "track_artist_name")
    val artistName: String,
    @ColumnInfo(name = "track_collection_name")
    val collectionName: String?,
    @ColumnInfo(name = "track_release_date")
    val releaseDate: String?,
    @ColumnInfo(name = "track_genre_name")
    val primaryGenreName: String,
    @ColumnInfo(name = "track_country")
    val country: String,
    @ColumnInfo(name = "track_time_millis")
    val trackTimeMillis: Long,
    @ColumnInfo(name = "track_preview_url")
    val previewUrl: String?,
    @ColumnInfo(name = "track_add_time")
    val timeOfAdittion: Long
)
