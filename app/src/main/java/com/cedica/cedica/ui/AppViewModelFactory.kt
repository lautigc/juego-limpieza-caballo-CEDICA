package com.cedica.cedica.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.DB
import com.cedica.cedica.ui.home.MainMenuViewModel
import com.cedica.cedica.ui.profile.ProfileListScreenViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelFactory {
    val Factory = viewModelFactory {
        initializer {
            MainMenuViewModel(
                userID = runBlocking { Session.getUserID().first() },
                db = DB,
            )
        }

        initializer {
            ProfileListScreenViewModel(
                currentUserID = Session.getUserID(),
                users = DB.userDao().getAllUsers(),
                patients = DB.patientDao().getAllPatients(),
                therapists = DB.therapistDao().getAllTherapists(),
            )
        }
    }
}