package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.domain.settings.AppThemeInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

var darkTheme = false

class App : Application() {

    private lateinit var themeSwitch: AppThemeInteractor

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule)
        }
        Creator.initApplication(this)
        themeSwitch = Creator.provideAppThemeInteractor()
        switchTheme(themeSwitch.getStatusSwitchFromShared())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}