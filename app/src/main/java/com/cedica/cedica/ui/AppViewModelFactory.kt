package com.cedica.cedica.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.DB
import com.cedica.cedica.ui.home.MainMenuViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelFactory {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            MainMenuViewModel(
                userID = runBlocking { Session.getUserID().first() },
                db = DB,
            )
        }
    }
}