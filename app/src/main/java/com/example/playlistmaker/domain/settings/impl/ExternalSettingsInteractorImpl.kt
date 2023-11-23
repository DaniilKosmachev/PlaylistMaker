package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.ExternalSettingsInteractor
import com.example.playlistmaker.domain.settings.ExternalSettingsRepository

class ExternalSettingsInteractorImpl(private val repository: ExternalSettingsRepository):
    ExternalSettingsInteractor {
    override fun shareApp() {
        repository.shareApp()
    }

    override fun writeToSupport() {
        repository.writeToSupport()
    }

    override fun userContract() {
        repository.userContract()
    }
}