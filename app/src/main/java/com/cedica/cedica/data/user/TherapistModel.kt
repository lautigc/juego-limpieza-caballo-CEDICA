package com.cedica.cedica.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
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


@Dao
interface TherapistDao {
    @Insert
    suspend fun insert(therapist: Therapist): Long

    @Update
    suspend fun update(therapist: Therapist)

    @Delete
    suspend fun delete(therapist: Therapist)

    @Query("SELECT * FROM Therapist")
    fun getAllTherapists(): Flow<List<Therapist>>
}