package com.cedica.cedica.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userID"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class Therapist(
    @PrimaryKey val userID: Int,
)

data class UserTherapist(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userID"
    )
    val therapist: Therapist
)

@Dao
interface TherapistDao {
    @Insert
    suspend fun insert(therapist: Therapist): Long

    @Update
    suspend fun update(therapist: Therapist)

    @Delete
    suspend fun delete(therapist: Therapist)

    @Query("SELECT * FROM Therapist")
    fun getAll(): Flow<List<Therapist>>

    @Query("SELECT * FROM User")
    fun getAllUserTherapist(): Flow<List<UserTherapist>>
}