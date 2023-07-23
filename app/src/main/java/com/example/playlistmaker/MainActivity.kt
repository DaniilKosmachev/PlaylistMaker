package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_settings = findViewById<Button>(R.id.setting_window)
        val button_search = findViewById<Button>(R.id.search_window)
        val button_library = findViewById<Button>(R.id.library_window)
        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(button: View?) {
                val openSettingsWindow = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(openSettingsWindow)
            }
        }
        button_search.setOnClickListener {
            val openSearchWindow = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(openSearchWindow)
        }
        button_settings.setOnClickListener(buttonClickListener)
        button_library.setOnClickListener(this@MainActivity)
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