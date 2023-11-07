package com.example.playlistmaker.domain.api.app_theme

interface AppThemeRepository {
    fun getStatusSwitchFromShared(): Boolean
    fun writeStatusSwitchToShared(status: Boolean)
}