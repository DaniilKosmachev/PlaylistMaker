package com.example.playlistmaker.ui.main.view_model

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.ui.library.activity.LibraryActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity
import com.example.playlistmaker.ui.tracks.activity.SearchActivity

class MainActivityViewModel(val context: Context): ViewModel() {

    fun openSettingsWindow() {
        context.startActivity(Intent(context, SettingsActivity::class.java))
    }

    fun openSearchWindow() {
        context.startActivity(Intent(context, SearchActivity::class.java))
    }

    fun openLibraryWindow() {
        context.startActivity(Intent(context, LibraryActivity::class.java))
    }

}