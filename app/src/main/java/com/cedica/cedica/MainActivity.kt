package com.cedica.cedica

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.cedica.cedica.core.navigation.NavigationWrapper
import com.cedica.cedica.core.session.SessionRepository
import com.cedica.cedica.core.session.dataStore
import com.cedica.cedica.ui.theme.CedicaTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking { Log.d(TAG, SessionRepository(dataStore).userID.first().toString() + "Hola") }
        SessionRepository(dataStore).userID.collectAsState()

        setContent {
            CedicaTheme {
                NavigationWrapper()
            }
        }
    }
}

