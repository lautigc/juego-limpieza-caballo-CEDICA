package com.cedica.cedica.data.user

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Query
import com.cedica.cedica.data.generic.BaseDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Entity(
    tableName = "PlaySession",
    indices = [
        Index(value = ["date"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = arrayOf("userID"),
            childColumns = arrayOf("userID"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class PlaySession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val difficultyLevel: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val timeSpent: Int,
    val userID: Long,
)

@Dao
interface PlaySessionDao: BaseDao<PlaySession>  {
    @Query("SELECT * FROM PlaySession")
    fun getAllPlaySessions(): Flow<List<PlaySession>>

    @Query("SELECT * FROM PlaySession WHERE id = :id")
    suspend fun getByID(id: Long): PlaySession
}
