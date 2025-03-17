package com.cedica.cedica.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cedica.cedica.data.seed.patients_seed
import com.cedica.cedica.data.seed.therapists_seed
import com.cedica.cedica.data.seed.users_seed
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.PatientDao
import com.cedica.cedica.data.user.PlaySession
import com.cedica.cedica.data.user.PlaySessionDao
import com.cedica.cedica.data.user.Therapist
import com.cedica.cedica.data.user.TherapistDao
import com.cedica.cedica.data.user.User
import com.cedica.cedica.data.user.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private val DB_NAME = "cedica_db"

interface RepositoryDao {
    fun userDao(): UserDao
    fun therapistDao(): TherapistDao
    fun patientDao(): PatientDao
    fun playSessionDao(): PlaySessionDao
}

/**
 * Database class with a singleton Instance object.
 */
@Database(
    entities = [
        User::class,
        Therapist::class,
        Patient::class,
        PlaySession::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(GlobalConverters::class)
abstract class AppDatabase : RoomDatabase(), RepositoryDao {

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .addCallback(
                        object: Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                CoroutineScope(Dispatchers.IO).launch {
                                    Instance?.userDao()?.insert(users_seed)
                                    Instance?.therapistDao()?.insert(therapists_seed)
                                    Instance?.patientDao()?.insert(patients_seed)
                                }
                            }
                        }
                    )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
object DB: RepositoryDao {

    // Must be initialized before use, if not, it will throw an exception for lateinit.
    private lateinit var db: AppDatabase

    /**
     * Initialize the database.
     */
    fun init(context: Context) {
        this.db = AppDatabase.getDatabase(context)
    }

    override fun userDao(): UserDao {
        return this.db.userDao()
    }

    override fun therapistDao(): TherapistDao {
        return this.db.therapistDao()
    }

    override fun patientDao(): PatientDao {
        return this.db.patientDao()
    }

    override fun playSessionDao(): PlaySessionDao {
        return this.db.playSessionDao()
    }
}