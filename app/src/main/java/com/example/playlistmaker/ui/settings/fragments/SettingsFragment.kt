package com.example.playlistmaker.ui.settings.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.themeStatus.observe(viewLifecycleOwner) {
            binding.themeSwitch.isChecked = it
        }
        themeSwitchClickListener()
        writeToSupportLinearClickListener()
        shareAppLinearClickListener()
        userContractLinearClickListener()
    }

    private fun themeSwitchClickListener() {
        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.writeToSharedStatusThemeApp(checked)
        }
    }

    private fun writeToSupportLinearClickListener() {
        binding.linearWriteToSupport.setOnClickListener {
            viewModel.writeToSupport()
        }
    }

    private fun shareAppLinearClickListener() {
        binding.linearShareApp.setOnClickListener {
            viewModel.shareApp()
        }
    }

    private fun userContractLinearClickListener() {
        binding.linearUserContract.setOnClickListener {
            viewModel.userContract()
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

}