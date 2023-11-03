package com.example.playlistmaker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.library.LibraryActivity
import com.example.playlistmaker.ui.settings.SettingsActivity
import com.example.playlistmaker.ui.tracks.SearchActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openSettingsActivityButtonClick()
        openSearchActivityButtonClick()
        binding.libraryWindow.setOnClickListener(this@MainActivity)
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

    override fun onClick(button: View?) {
        when (button?.id) {
            R.id.library_window -> {
                val openLibraryWindow = Intent(this@MainActivity, LibraryActivity::class.java)
                startActivity(openLibraryWindow)
            }
        }
    }
}