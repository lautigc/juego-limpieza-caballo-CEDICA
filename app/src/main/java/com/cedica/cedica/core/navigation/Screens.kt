package com.cedica.cedica.core.navigation

import com.cedica.cedica.data.user.User
import kotlinx.serialization.Serializable

@Serializable
object Menu

@Serializable
object Configuration

@Serializable
object Game

@Serializable
object About

@Serializable
object Stats

@Serializable
object CreateTherapistScreen

@Serializable
object CreatePatientScreen

@Serializable
data class UserListScreen(val alertNotification: String? = null)

@Serializable
data class UserSetting (val userID: Long)

@Serializable
data class EditTherapistScreen(val userID: Long)

@Serializable
data class EditPatientScreen(val userID: Long)