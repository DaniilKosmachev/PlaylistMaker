package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.library.activity.LibraryActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity
import com.example.playlistmaker.ui.tracks.activity.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openSettingsActivityButtonClick()
        openSearchActivityButtonClick()
        openLibraryWindow()
    }

    private fun openLibraryWindow() {
        binding.libraryWindow.setOnClickListener {
            val openLibraryWindow = Intent(this@MainActivity, LibraryActivity::class.java)
            startActivity(openLibraryWindow)
        }
    }

    private fun openSettingsActivityButtonClick() {
        val buttonClickListener: View.OnClickListener = View.OnClickListener {
            val openSettingsWindow = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(openSettingsWindow)
        }
        binding.settingWindow.setOnClickListener(buttonClickListener)
    }

    private fun openSearchActivityButtonClick() {
        binding.searchWindow.setOnClickListener {
            val openSearchWindow = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(openSearchWindow)
        }
    }
}