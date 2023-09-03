package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout

class SettingsActivity : AppCompatActivity() {
    private lateinit var buttonBackFromMainActivity:ImageButton
    private lateinit var linearLayoutWriteToSupport:LinearLayout
    private lateinit var linerLayoutUserContract:LinearLayout
    private lateinit var linerLayoutShareApp:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        buttonBackFromMainActivity = findViewById(R.id.backToMainActivityViewButton)
        linearLayoutWriteToSupport = findViewById(R.id.linearWriteToSupport)
        linerLayoutUserContract = findViewById(R.id.linearUserContract)
        linerLayoutShareApp = findViewById(R.id.linearShareApp)
        backToMainActivityClickLisnter()
        writeToSupportLinearClickListener()
        shareAppLinearClickListener()
        userContractLinearClickListener()
    }
    private fun backToMainActivityClickLisnter() {
        buttonBackFromMainActivity.setOnClickListener {
            finish()
        }
    }
    private fun writeToSupportLinearClickListener() {
        linearLayoutWriteToSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data= Uri.parse(getString(R.string.share_data_mailto))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_support)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_support))
                putExtra(Intent.EXTRA_TEXT,getString(R.string.message_for_support))
                startActivity(this)
            }

        }
    }
    private fun shareAppLinearClickListener() {
        linerLayoutShareApp.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,getString(R.string.share_app_url))
                startActivity(this)
            }
        }
    }
    private fun userContractLinearClickListener() {
        linerLayoutUserContract.setOnClickListener {
            Intent(Intent.ACTION_VIEW,Uri.parse(getString(R.string.user_contract_url))).apply {
                startActivity(this)
            }
        }
    }

}