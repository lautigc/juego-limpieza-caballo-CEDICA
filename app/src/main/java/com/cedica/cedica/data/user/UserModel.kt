package com.cedica.cedica.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import com.cedica.cedica.data.configuration.PersonalConfiguration
import com.cedica.cedica.data.generic.BaseDao
import com.cedica.cedica.data.permissions.Role
import kotlinx.coroutines.flow.Flow


val LoadingUser = User(
    id = -1,
    role = Role.GUEST,
    firstName = "Cargando",
    lastName = "",
    username = "Cargando"
)

@Entity(
    tableName = "User",
    indices = [
        Index(value = ["username"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: Role,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val username: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val firstName: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val lastName: String,
    @Embedded val personalConfiguration: PersonalConfiguration = PersonalConfiguration(),
) {
    val fullName: String
        get() = "$firstName $lastName"
}

@Dao
abstract class UserDao: BaseDao<User> {

    @Query("SELECT * FROM User")
    abstract fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id = :id")
    abstract suspend fun getByID(id: Long): User

    @Query(
        "SELECT * FROM User " +
        "WHERE firstname = :firstName AND lastname = :lastName"
    )
    abstract suspend fun getByFullName(firstName: String, lastName: String): User?

    @Query("SELECT * FROM User WHERE username = :username")
    abstract suspend fun getByUsername(username: String): User?

    suspend fun existsByFullName(firstName: String, lastName: String): Boolean {
        return getByFullName(firstName, lastName) != null
    }

    suspend fun existByUsername(username: String): Boolean {
        return getByUsername(username) != null
    }
}