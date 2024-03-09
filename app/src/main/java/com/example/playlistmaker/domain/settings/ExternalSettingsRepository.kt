package com.example.playlistmaker.domain.settings


interface ExternalSettingsRepository {
    fun shareApp()

    fun writeToSupport()

    fun userContract()

    fun sharePlaylist(messageText: String)
}