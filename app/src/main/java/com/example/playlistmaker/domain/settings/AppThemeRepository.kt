package com.example.playlistmaker.domain.settings

interface AppThemeRepository {
    fun getStatusSwitchFromShared(): Boolean
    fun writeStatusSwitchToShared(status: Boolean)
}