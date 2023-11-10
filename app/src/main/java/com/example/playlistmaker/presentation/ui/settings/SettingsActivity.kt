package com.example.playlistmaker.presentation.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.api.app_theme.AppThemeInteractor

@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var appThemeSwitch: AppThemeInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appThemeSwitch = Creator.provideAppThemeInteractor()
        backToMainActivityClickListener()
        writeToSupportLinearClickListener()
        shareAppLinearClickListener()
        userContractLinearClickListener()
        themeSwitchClickListener()
        checkSwitchThemeStatus()
    }

    private fun checkSwitchThemeStatus() {
        binding.themeSwitch.isChecked = appThemeSwitch.getStatusSwitchFromShared()
    }

    private fun themeSwitchClickListener() {
        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }

    private fun backToMainActivityClickListener() {
        binding.backToMainActivityViewButton.setOnClickListener {
            finish()
        }
    }

    private fun writeToSupportLinearClickListener() {
        binding.linearWriteToSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.share_data_mailto))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_support)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_support))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.message_for_support))
                startActivity(this)
            }

        }
    }

    private fun shareAppLinearClickListener() {
        binding.linearShareApp.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_url))
                startActivity(this)
            }
        }
    }

    private fun userContractLinearClickListener() {
        binding.linearUserContract.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_contract_url))).apply {
                startActivity(this)
            }
        }
    }
}