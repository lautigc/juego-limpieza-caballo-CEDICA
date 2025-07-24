package com.cedica.cedica.data.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.repository.RepositoryProvider
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.data.user.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

enum class Permission() {
    GAME_PLAY,

    PATIENT_CREATE,
    PATIENT_DELETE,
    PATIENT_EDIT,
    PATIENT_CONFIG,

    THERAPIST_CREATE,
    THERAPIST_DELETE,
    THERAPIST_EDIT,

    CONFIG_CREATE,

    STATS_READ,
}

enum class Role(private val permissions: List<Permission>) {
    ADMIN(listOf(
        Permission.PATIENT_CREATE,
        Permission.PATIENT_DELETE,
        Permission.PATIENT_CONFIG,
        Permission.PATIENT_EDIT,
        Permission.THERAPIST_CREATE,
    )),
    USER(listOf(
        Permission.GAME_PLAY,
        Permission.STATS_READ,
        Permission.CONFIG_CREATE,
    )),
    GUEST(listOf(
        Permission.GAME_PLAY,
        Permission.CONFIG_CREATE
    ));

    fun hasPermission(permissions: List<Permission>): Boolean
        = this.permissions.containsAll(permissions)

    fun hasPermission(permission: Permission): Boolean
        = this.permissions.contains(permission)
}


@Composable
fun HasPermission(
    permissions: List<Permission>,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var hasPermission by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val user = getUser()
        hasPermission = user.role.hasPermission(permissions)
    }

    if (hasPermission) {
        content()
    }
}

@Composable
fun HasPermission(
    permission: Permission,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var hasPermission by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val user = getUser()
        hasPermission = user.role.hasPermission(permission)
    }

    if (hasPermission) {
        content()
    }
}

private suspend fun getUser(): User {
    val userRepository: UserRepository = RepositoryProvider.userRepository
    val userID = Session.getUserID().first() // Asegurar que esto es un Flow y no LiveData
    val user = userRepository.getByID(userID)
    return user
}

fun hasPermission(permission: Permission): Boolean = runBlocking{
    getUser().role.hasPermission(permission)
}

fun hasPermission(permission: List<Permission>): Boolean = runBlocking{
    getUser().role.hasPermission(permission)
}