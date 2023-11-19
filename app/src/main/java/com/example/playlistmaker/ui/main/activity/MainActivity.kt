package com.example.playlistmaker.ui.main.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.main.view_model.MainActivityViewModel
import com.example.playlistmaker.ui.main.view_model.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, MainActivityViewModelFactory(this))[MainActivityViewModel::class.java]
        openSettingsActivityButtonClick()
        openSearchActivityButtonClick()
        openLibraryWindow()
    }

    private fun openLibraryWindow() {
        binding.libraryWindow.setOnClickListener {
            viewModel.openLibraryWindow()
        }
    }

    private fun openSettingsActivityButtonClick() {
        val buttonClickListener: View.OnClickListener = View.OnClickListener {
            viewModel.openSettingsWindow()
        }
        binding.settingWindow.setOnClickListener(buttonClickListener)
    }

    private fun openSearchActivityButtonClick() {
        binding.searchWindow.setOnClickListener {
            viewModel.openSearchWindow()
        }
    }
}