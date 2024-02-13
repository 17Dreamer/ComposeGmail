package com.dreamtech.compose.gmail

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GmailApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}