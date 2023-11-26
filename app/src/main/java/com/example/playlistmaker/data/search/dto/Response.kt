package com.example.playlistmaker.data.search.dto

import com.example.playlistmaker.domain.search.model.ResponceStatus
import org.koin.java.KoinJavaComponent.getKoin

open class Response {
    var resultResponse: ResponceStatus = getKoin().get()
}