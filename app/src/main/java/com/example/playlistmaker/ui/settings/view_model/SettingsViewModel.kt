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

    private val themeStatusMutable = MutableLiveData<Boolean>()

    val themeStatus: LiveData<Boolean> = themeStatusMutable

    init {
        themeStatusMutable.value = appThemeInteractor.getStatusSwitchFromShared()
    }

    fun writeToSharedStatusThemeApp(checked: Boolean) {
        themeStatusMutable.value = checked
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