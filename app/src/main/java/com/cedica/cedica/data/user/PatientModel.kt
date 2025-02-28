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
import java.util.Date

// ISO/IEC 5218
enum class Gender(val code: Int) {
    NOT_KNOWN(0),
    MALE(1),
    FEMALE(2),
    NOT_APPLICABLE(9)
}

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
data class Patient(
    @PrimaryKey val userID: Int,
    val gender: Gender,
    val observations: String,
    val birthDate: Date,
)

data class UserPatient(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userID"
    )
    val patient: Patient
)

@Dao
interface PatientDao {
    @Insert
    suspend fun insert(patient: Patient): Long

    @Update
    suspend fun update(patient: Patient): Unit

    @Delete
    suspend fun delete(patient: Patient): Unit

    @Query("SELECT * FROM User")
    suspend fun getAllUserPatient(): UserPatient

    @Query("SELECT * FROM Patient")
    fun getAll(): Flow<List<Patient>>

    @Query("SELECT * FROM Patient WHERE userID = :userID")
    suspend fun getPK(userID: Int): Patient
}