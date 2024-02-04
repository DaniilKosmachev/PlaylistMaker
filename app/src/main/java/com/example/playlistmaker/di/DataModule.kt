package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.App
import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.ITunesApi
import com.example.playlistmaker.data.search.impl.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)//ДОБАВЛЕНО
    }

    single(named("historyShared")) {
        androidContext()
            .getSharedPreferences("history_search_track", Context.MODE_PRIVATE)//добавлено!
    }

    single(named("themeShared")) {
        androidContext()
            .getSharedPreferences("theme_shared_preferences", Context.MODE_PRIVATE)//добавлено!!
    }

    factory {
        Gson()//добавлено!
    }

    factory {
        MediaPlayer()//добавлено!
    }

    factory<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single(named("context")) {
        App()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "favouriteTracksDb.db")
            .build()
    }



}