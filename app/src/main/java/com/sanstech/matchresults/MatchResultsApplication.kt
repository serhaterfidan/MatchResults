package com.sanstech.matchresults

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MatchResultsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}