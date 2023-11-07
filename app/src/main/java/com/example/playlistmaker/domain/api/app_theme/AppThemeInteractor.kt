package com.example.playlistmaker.domain.api.app_theme

interface AppThemeInteractor {
    fun getStatusSwitchFromShared(): Boolean
    fun writeStatusSwitchToShared(status: Boolean)
}