package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import com.google.android.material.switchmaterial.SwitchMaterial

@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingsActivity : AppCompatActivity() {

    private lateinit var buttonBackFromMainActivity: ImageButton
    private lateinit var linearLayoutWriteToSupport: LinearLayout
    private lateinit var linerLayoutUserContract: LinearLayout
    private lateinit var linerLayoutShareApp: LinearLayout
    private lateinit var themeSwitch: Switch
    private lateinit var sharePref: SharedPreferences
    private lateinit var classSwitchAppTheme: AppThemeSwitch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initializeViewElementSettingsActivity()
        backToMainActivityClickListener()
        writeToSupportLinearClickListener()
        shareAppLinearClickListener()
        userContractLinearClickListener()
        themeSwitchClickListener()
        initializeVariable()
        checkSwitchThemeStatus()
    }

    private fun checkSwitchThemeStatus() {
        themeSwitch.isChecked = classSwitchAppTheme.getStatusSwitchFromShared()
    }

    private fun initializeVariable() {
        sharePref = getSharedPreferences(THEME_SWITCH_FILE_NAME, MODE_PRIVATE)
        classSwitchAppTheme = AppThemeSwitch(sharePref)
    }

    private fun initializeViewElementSettingsActivity() {
        buttonBackFromMainActivity = findViewById(R.id.backToMainActivityViewButton)
        linearLayoutWriteToSupport = findViewById(R.id.linearWriteToSupport)
        linerLayoutUserContract = findViewById(R.id.linearUserContract)
        linerLayoutShareApp = findViewById(R.id.linearShareApp)
        themeSwitch = findViewById(R.id.theme_Switch)
    }

    private fun themeSwitchClickListener() {
        themeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }

    private fun backToMainActivityClickListener() {
        buttonBackFromMainActivity.setOnClickListener {
            finish()
        }
    }

    private fun writeToSupportLinearClickListener() {
        linearLayoutWriteToSupport.setOnClickListener {
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
        linerLayoutShareApp.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_url))
                startActivity(this)
            }
        }
    }

    private fun userContractLinearClickListener() {
        linerLayoutUserContract.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_contract_url))).apply {
                startActivity(this)
            }
        }
    }

    companion object {
        const val THEME_SWITCH_FILE_NAME = "theme_shared_preferences"
    }
}