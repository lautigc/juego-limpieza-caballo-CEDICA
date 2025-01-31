package com.cedica.cedica

import android.app.Application
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.core.session.sessionStore

/*
 * Custom app entry point for manual dependency injection
 */
class CedicaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initDataStores()
    }

    private fun initDataStores(): Unit {
        Session.init(this.sessionStore)
    }
}