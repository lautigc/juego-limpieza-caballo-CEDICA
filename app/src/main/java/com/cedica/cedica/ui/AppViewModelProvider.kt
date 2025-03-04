package com.cedica.cedica.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.DB
import com.cedica.cedica.data.repository.RepositoryProvider
import com.cedica.cedica.ui.home.MainMenuViewModel
import com.cedica.cedica.ui.profile.ProfileListScreenViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            MainMenuViewModel(
                userID = Session.getUserID(),
                userRepository = RepositoryProvider.userRepository
            )
        }

        initializer {
            ProfileListScreenViewModel(
                session = Session,
                users = DB.userDao().getAllUsers(),
                patients = DB.patientDao().getAll(),
                therapists = DB.therapistDao().getAll(),
            )
        }
    }
}