package com.cedica.cedica.core.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cedica.cedica.core.guestData.GuestUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Definición de datastore
private const val DATA_STORE_NAME = "SESSION"
val Context.sessionStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

/*
* Session object to management user session
 */
object Session {
    // DataStore must initialize in root UI
    private lateinit var dataStore: DataStore<Preferences>

    /*
    * Initialize Singleton object. If you don't call this method, lateinit exception will be thrown
     */
    fun init(dataStore: DataStore<Preferences>) {
        this.dataStore = dataStore
    }

    // Key-values for datastore
    private val USER_ID = longPreferencesKey("USER_ID")

    suspend fun setUserID(userID: Long) {
        this.dataStore.edit { preferences ->
            preferences[USER_ID] = userID
        }
    }

    fun getUserID(): Flow<Long> {
        return this.dataStore.data.map { preferences ->
            preferences[USER_ID] ?: GuestUser.id
        }
    }
}