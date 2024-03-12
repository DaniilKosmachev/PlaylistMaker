package com.example.playlistmaker.domain.settings

interface ExternalSettingsInteractor {
    fun shareApp()

    fun writeToSupport()

    fun userContract()

    fun sharePlaylist(mainPlaylistInfo: String,trackCount: Int, formattingListTracks: String)
}