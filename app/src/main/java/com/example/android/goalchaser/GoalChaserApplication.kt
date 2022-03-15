package com.example.android.goalchaser

import android.app.Application
import timber.log.Timber

class GoalChaserApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // use Timber for more effective logging during debug process
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}