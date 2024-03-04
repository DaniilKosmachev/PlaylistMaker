package com.example.playlistmaker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.dao.PlaylistDao
import com.example.playlistmaker.data.dao.TrackDao
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.db.entity.TracksInPlaylistsEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, TracksInPlaylistsEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

}