package com.cedica.cedica.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.repository.RepositoryProvider
import com.cedica.cedica.ui.profile.ProfileListScreenViewModel
import com.cedica.cedica.ui.profile.create.CreatePatientFormViewModel
import com.cedica.cedica.ui.profile.create.CreateTherapistFormViewModel
import com.cedica.cedica.ui.profile.create.CreateUserFormViewModel
import com.cedica.cedica.ui.utils.view_models.UserViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {

    /**
     * Factory to create instance of ViewModel that not require any parameter
     */
    val Factory = viewModelFactory {

        initializer {
            UserViewModel(
                session = Session,
                userRepository = RepositoryProvider.userRepository
            )
        }

        initializer {
            CreateUserFormViewModel(
                userRepository = RepositoryProvider.userRepository
            )
        }

        initializer {
            CreateTherapistFormViewModel(
                therapistRepository = RepositoryProvider.therapistRepository,
                userRepository = RepositoryProvider.userRepository
            )
        }

        initializer {
            CreatePatientFormViewModel(
                patientRepository = RepositoryProvider.patientRepository,
                userRepository = RepositoryProvider.userRepository
            )
        }
    }

    /**
     * Functions factories to create instance of ViewModel that require parameters
     */
    fun profileListScreenViewModelFactory(notification: String? = null): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                ProfileListScreenViewModel(
                    session = Session,
                    userRepository = RepositoryProvider.userRepository,
                    patientRepository = RepositoryProvider.patientRepository,
                    therapistRepository = RepositoryProvider.therapistRepository,
                    notification = notification,
                )
            }
        }
    }
}