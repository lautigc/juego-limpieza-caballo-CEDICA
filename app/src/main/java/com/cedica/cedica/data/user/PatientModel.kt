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
import java.util.Date

// ISO/IEC 5218
enum class Gender(val code: Int, private val toStr: String) {
    NOT_KNOWN(0, "No definido"),
    MALE(1, "Masculino"),
    FEMALE(2, "Femenino"),
    NOT_APPLICABLE(9, "No aplicable");

    override fun toString(): String = this.toStr
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
    @PrimaryKey val userID: Long,
    val gender: Gender,
    val observations: String,
    val birthDate: Date,
)

data class UserPatient(
    @Embedded val patient: Patient,
    @Relation(
        parentColumn = "userID",
        entityColumn = "id"
    )
    val user: User
)

@Dao
interface PatientDao: BaseDao<Patient> {

    @Query("SELECT * FROM Patient")
    fun getAllUserPatient(): Flow<List<UserPatient>>

    @Query("SELECT * FROM Patient")
    fun getAll(): Flow<List<Patient>>

    @Query("SELECT * FROM Patient WHERE userID = :userID")
    suspend fun getByID(userID: Long): Patient

    @Query("SELECT * FROM PlaySession WHERE userID = :userID")
    fun getAllPlaySessions(userID: Long): Flow<List<PlaySession>>
}