package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var buttonSettings:Button
    private lateinit var buttonSearch:Button
    private lateinit var buttonLibrary:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonSettings = findViewById(R.id.setting_window)
        buttonSearch = findViewById(R.id.search_window)
        buttonLibrary = findViewById(R.id.library_window)
        val buttonClickListener: View.OnClickListener = View.OnClickListener {
            val openSettingsWindow = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(openSettingsWindow)
        }
        buttonSearch.setOnClickListener {
            val openSearchWindow = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(openSearchWindow)
        }
        buttonSettings.setOnClickListener(buttonClickListener)
        buttonLibrary.setOnClickListener(this@MainActivity)
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