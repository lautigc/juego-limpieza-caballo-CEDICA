package com.cedica.cedica.data.repository.interfaces

import com.cedica.cedica.data.user.User

interface UserRepository: BaseRepository<User> {

    suspend fun existsByFullName(firstName: String, lastName: String): Boolean

    suspend fun existByUsername(username: String): Boolean
}