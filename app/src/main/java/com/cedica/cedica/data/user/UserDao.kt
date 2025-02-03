package com.cedica.cedica.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(note: User)

    @Update
    suspend fun update(note: User)

    @Delete
    suspend fun delete(note: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>
}