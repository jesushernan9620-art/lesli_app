package com.lesli.mobile

import android.app.Application
import com.lesli.mobile.di.AppContainer
import com.lesli.mobile.di.AppContainerImpl

class LesliApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
