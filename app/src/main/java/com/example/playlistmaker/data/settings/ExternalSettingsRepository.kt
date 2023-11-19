package com.example.playlistmaker.data.settings


interface ExternalSettingsRepository {
    fun shareApp()
    fun writeToSupport()
    fun userContract()
}