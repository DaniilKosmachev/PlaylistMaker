package com.example.playlistmaker.ui.settings.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SettingsViewModelFactory(var context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            Creator.provideExternalSettingsInteractor(context = context),
            Creator.provideAppThemeInteractor()
        ) as T
    }
}