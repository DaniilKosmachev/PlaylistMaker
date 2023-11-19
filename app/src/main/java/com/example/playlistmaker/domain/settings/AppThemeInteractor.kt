package com.example.playlistmaker.domain.settings

interface AppThemeInteractor {
    fun getStatusSwitchFromShared(): Boolean
    fun writeStatusSwitchToShared(status: Boolean)
}