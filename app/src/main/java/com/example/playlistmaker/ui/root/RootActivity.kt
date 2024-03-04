package com.example.playlistmaker.ui.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.RootActivityBinding

class RootActivity: AppCompatActivity() {

    private lateinit var binding: RootActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RootActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, distination, _ ->
            when(distination.id) {
                R.id.createPlaylistFragment, R.id.audioPlayerFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

}