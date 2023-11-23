package com.example.playlistmaker.data.settings.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.settings.ExternalSettingsRepository

class ExternalSettingsRepositoryImpl(private val context: Context): ExternalSettingsRepository {
    override fun shareApp() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_url))
            context.startActivity(this)
        }
    }

    override fun writeToSupport() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(context.getString(R.string.share_data_mailto))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email_support)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.theme_support))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.message_for_support))
            context.startActivity(this)
        }

    }

    override fun userContract() {
        Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.user_contract_url))).apply {
            context.startActivity(this)
        }
    }

}