package org.nootnoot.project

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.nootnoot.project.di.initKoin

class NootnootApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@NootnootApplication)
        }
    }
}