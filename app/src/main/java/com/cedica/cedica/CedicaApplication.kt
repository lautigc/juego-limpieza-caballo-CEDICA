package com.cedica.cedica

import android.app.Application
import com.cedica.cedica.core.configuration.GlobalConfiguration
import com.cedica.cedica.core.configuration.globalConfigurationStore
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.core.session.sessionStore
import com.cedica.cedica.data.DB

/*
 * Custom app entry point for manual dependency injection
 */
class CedicaApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initRepositories()
    }

    private fun initRepositories(): Unit {
        Session.init(this.sessionStore)
        GlobalConfiguration.init(this.globalConfigurationStore)
        DB.init(this)
    }
}