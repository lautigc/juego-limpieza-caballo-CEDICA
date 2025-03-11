package com.cedica.cedica.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Query
import com.cedica.cedica.data.generic.BaseDao
import com.cedica.cedica.data.permissions.Role
import kotlinx.coroutines.flow.Flow


val GuestUser = User(
    id = 0,
    role = Role.GUEST,
    firstName = "Invitado",
    lastName = "",
)

val LoadingUser = User(
    id = -1,
    role = Role.GUEST,
    firstName = "Cargando",
    lastName = "",
)

@Entity(
    tableName = "User",
    indices = [
        Index(value = ["firstName", "lastName"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: Role,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val firstName: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val lastName: String,
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

    suspend fun existsByFullName(firstName: String, lastName: String): Boolean {
        return getByFullName(firstName, lastName) != null
    }
}