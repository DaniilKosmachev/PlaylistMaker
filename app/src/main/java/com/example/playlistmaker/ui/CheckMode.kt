package com.example.playlistmaker.ui

import android.content.Context
import android.content.res.Configuration

fun Context.isNightModeOn(): Boolean =
    resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

fun Context.isPortrainSystemOrientatin(): Boolean =
    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT