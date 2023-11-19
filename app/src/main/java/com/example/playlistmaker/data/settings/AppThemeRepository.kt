package com.example.playlistmaker.data.settings

interface AppThemeRepository {
    fun getStatusSwitchFromShared(): Boolean
    fun writeStatusSwitchToShared(status: Boolean)
}