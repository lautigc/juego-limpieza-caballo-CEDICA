package com.cedica.cedica.data.user

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import com.cedica.cedica.data.generic.BaseDao
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
    @PrimaryKey val userID: Long,
)

data class UserTherapist(
    @Embedded val therapist: Therapist,
    @Relation(
        parentColumn = "userID",
        entityColumn = "id"
    )
    val user: User
)

@Dao
interface TherapistDao: BaseDao<Therapist> {

    @Query("SELECT * FROM Therapist")
    fun getAll(): Flow<List<Therapist>>

    @Query("SELECT * FROM Therapist")
    fun getAllUserTherapist(): Flow<List<UserTherapist>>

    @Query("SELECT * FROM Therapist WHERE userID = :userID")
    suspend fun getByID(userID: Long): Therapist
}