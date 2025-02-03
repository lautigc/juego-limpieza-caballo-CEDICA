package com.cedica.cedica.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cedica.cedica.data.user.User
import com.cedica.cedica.data.user.UserDao


private val DB_NAME = "cedica_db"

interface RepositoryDao {
    fun userDao(): UserDao
}

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(), RepositoryDao {

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
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
        db = AppDatabase.getDatabase(context)
    }

    override fun userDao(): UserDao {
        return db.userDao()
    }
}