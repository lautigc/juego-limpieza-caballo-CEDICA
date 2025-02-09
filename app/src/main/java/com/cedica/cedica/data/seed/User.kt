package com.cedica.cedica.data.seed

import com.cedica.cedica.data.permissions.Role
import com.cedica.cedica.data.user.User

val users_seed = listOf(
    User(id = 1, firstName = "John", lastName = "Doe", role = Role.ADMIN),
    User(id = 2, firstName = "Jane", lastName = "Doe", role = Role.USER),
    User(id = 3, firstName = "Alice", lastName = "Doe", role = Role.USER),
    User(id = 4, firstName = "Bob", lastName = "Doe".repeat(5), role = Role.USER),
    User(id = 5, firstName = "Charlie", lastName = "Doe", role = Role.USER),
    User(id = 6, firstName = "David", lastName = "Doe", role = Role.USER),
    User(id = 7, firstName = "Eve", lastName = "Doe", role = Role.USER),
    User(id = 8, firstName = "Frank", lastName = "Doe", role = Role.USER),
)