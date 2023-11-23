package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModelFactory


class SettingsActivity : AppCompatActivity() { //не меняет тему вьюхи если наследутеся от ComponentActivity()

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]
        backToMainActivityClickListener()
        writeToSupportLinearClickListener()
        shareAppLinearClickListener()
        userContractLinearClickListener()
        themeSwitchClickListener()

        viewModel.themeStatus.observe(this) {
            binding.themeSwitch.isChecked = it
        }
    }

    private fun themeSwitchClickListener() {
        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.writeToSharedStatusThemeApp(checked)
        }
    }

    private fun backToMainActivityClickListener() {
        binding.backToMainActivityViewButton.setOnClickListener {
            finish()
        }
    }

    private fun writeToSupportLinearClickListener() {
        binding.linearWriteToSupport.setOnClickListener {
            viewModel.writeToSupport()
        }
    }

    private fun shareAppLinearClickListener() {//*
        binding.linearShareApp.setOnClickListener {
            viewModel.shareApp()
        }
    }

    private fun userContractLinearClickListener() {
        binding.linearUserContract.setOnClickListener {
            viewModel.userContract()
        }
    }
}