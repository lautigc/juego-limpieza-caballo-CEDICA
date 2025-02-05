package com.cedica.cedica.data.seed

import com.cedica.cedica.data.permissions.Role
import com.cedica.cedica.data.user.User

val users_seed = listOf(
    User(firstName = "John", lastName = "Doe", role = Role.ADMIN),
    User(firstName = "Jane", lastName = "Doe", role = Role.USER),
    User(firstName = "Alice", lastName = "Doe", role = Role.USER),
    User(firstName = "Bob", lastName = "Doe".repeat(5), role = Role.USER),
    User(firstName = "Charlie", lastName = "Doe", role = Role.USER),
    User(firstName = "David", lastName = "Doe", role = Role.USER),
    User(firstName = "Eve", lastName = "Doe", role = Role.USER),
    User(firstName = "Frank", lastName = "Doe", role = Role.USER),
//    User(firstName = "Grace", lastName = "Doe", role = Role.USER),
//    User(firstName = "Heidi", lastName = "Doe", role = Role.USER),
)