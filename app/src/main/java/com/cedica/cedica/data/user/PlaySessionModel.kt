package com.cedica.cedica.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Entity(
    tableName = "PlaySession",
    indices = [
        Index(value = ["date"], unique = true)
    ]
)
data class PlaySession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDateTime,
    val difficultyLevel: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val timeSpent: Int,
)

@Dao
interface PlaySessionDao {
    @Insert
    suspend fun insert(playSession: PlaySession)

    @Update
    suspend fun update(playSession: PlaySession)

    @Delete
    suspend fun delete(playSession: PlaySession)

    @Query("SELECT * FROM PlaySession")
    fun getAllPlaySessions(): Flow<List<PlaySession>>

    @Query("SELECT * FROM PlaySession WHERE id = :id")
    suspend fun getByID(id: Int): PlaySession?
}
