package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.AppThemeInteractor
import com.example.playlistmaker.domain.settings.ExternalSettingsInteractor

class SettingsViewModel(
    val settingsInteractor: ExternalSettingsInteractor,
    val appThemeInteractor: AppThemeInteractor
): ViewModel() {

    private val _themeStatus = MutableLiveData<Boolean>()

    val themeStatus: LiveData<Boolean> = _themeStatus

    init {
        _themeStatus.value = appThemeInteractor.getStatusSwitchFromShared()
    }

    fun writeToSharedStatusThemeApp(checked: Boolean) {
        _themeStatus.value = checked
        appThemeInteractor.writeStatusSwitchToShared(checked)
    }

    fun writeToSupport() {
        settingsInteractor.writeToSupport()
    }

    fun shareApp() {
        settingsInteractor.shareApp()
    }

    fun userContract() {
        settingsInteractor.userContract()
    }
}