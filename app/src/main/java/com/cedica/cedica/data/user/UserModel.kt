package com.cedica.cedica.data.user

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

@Entity(
    tableName = "User",
    indices = [
        Index(value = ["firstName", "lastName"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)  val id: Int = 0,
    val role: Role,
    val firstName: String,
    val lastName: String,
)

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>
}