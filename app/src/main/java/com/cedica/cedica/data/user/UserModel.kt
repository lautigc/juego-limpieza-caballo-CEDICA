package com.cedica.cedica.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cedica.cedica.data.permissions.Role
import kotlinx.coroutines.flow.Flow


val GuestUser = User(
    id = 0,
    role = Role.GUEST,
    firstName = "Invitado",
    lastName = "",
)

@Entity(
    tableName = "User",
    indices = [
        Index(value = ["firstName", "lastName"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    val role: Role,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val firstName: String,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val lastName: String,
) {
    val fullName: String
        get() = "$firstName $lastName"
}

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Insert
    suspend fun insert(users: List<User>): List<Long>

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getByID(id: Int): User
}