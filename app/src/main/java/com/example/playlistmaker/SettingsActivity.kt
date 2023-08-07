package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.app.ShareCompat.IntentReader

class SettingsActivity : AppCompatActivity() {
    private lateinit var buttonBackFromMainActivity:ImageButton
    private lateinit var linearLayoutWriteToSupport:LinearLayout
    private lateinit var linerLayoutUserContract:LinearLayout
    private lateinit var linerLayoutShareApp:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        buttonBackFromMainActivity = findViewById<ImageButton>(R.id.backToMainActivityViewButton)
        linearLayoutWriteToSupport = findViewById<LinearLayout>(R.id.linearWriteToSupport)
        linerLayoutUserContract = findViewById<LinearLayout>(R.id.linearUserContract)
        linerLayoutShareApp = findViewById<LinearLayout>(R.id.linearShareApp)
        buttonBackFromMainActivity.setOnClickListener {
            finish()
        }
        linearLayoutWriteToSupport.setOnClickListener {
           Intent(Intent.ACTION_SENDTO).apply {
               data= Uri.parse(getString(R.string.share_data_mailto))
               putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_support)))
               putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_support))
               putExtra(Intent.EXTRA_TEXT,getString(R.string.message_for_support))
               startActivity(this)
           }

        }
        linerLayoutUserContract.setOnClickListener {
            Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.user_contract_url))).apply {
                startActivity(this)
            }
        }
        linerLayoutShareApp.setOnClickListener {
        Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            //setType(getString(R.string.share_app_url))
            putExtra(Intent.EXTRA_TEXT,getString(R.string.share_app_url))
            startActivity(this)
        }
        }

    }

}