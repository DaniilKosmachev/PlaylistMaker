package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.app.ShareCompat.IntentReader

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonBackFromMainActivity = findViewById<ImageButton>(R.id.backToMainActivityViewButton)
        buttonBackFromMainActivity.setOnClickListener {
            finish()
        }
    }

}