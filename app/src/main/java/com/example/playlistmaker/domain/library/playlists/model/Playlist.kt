package com.example.playlistmaker.domain.library.playlists.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Int?,
    val name: String,
    val description: String?,
    val uri: String?,
    val tracksId: String?,
    val count: Int?
): Parcelable
