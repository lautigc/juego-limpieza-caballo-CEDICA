package com.cedica.cedica.data.user

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "User",
    indices = [
        Index(value = ["firstName", "lastName"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    val firstName: String,
    val lastName: String,
)