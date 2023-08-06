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
        val linearLayoutWriteToSupport = findViewById<LinearLayout>(R.id.linearWriteToSupport)
        val linerLayoutUserContract = findViewById<LinearLayout>(R.id.linearUserContract)
        val linerLayoutShareApp = findViewById<LinearLayout>(R.id.linearShareApp)
        buttonBackFromMainActivity.setOnClickListener {
            finish()
        }
        linearLayoutWriteToSupport.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data= Uri.parse(getString(R.string.share_data_mailto))
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_support)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_support))
            shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.message_for_support))
            startActivity(shareIntent)
        }
        linerLayoutUserContract.setOnClickListener {
            val url = Uri.parse(getString(R.string.user_contract_url))
            val openUserContractIntent = Intent(Intent.ACTION_VIEW,url)
            startActivity(openUserContractIntent)
        }
        linerLayoutShareApp.setOnClickListener {
            val shareApp = Intent(Intent.ACTION_SEND)
            shareApp.setType(getString(R.string.share_app_url))
            shareApp.putExtra(Intent.EXTRA_TEXT,getString(R.string.share_app_url))
            startActivity(shareApp)
        }

    }

}