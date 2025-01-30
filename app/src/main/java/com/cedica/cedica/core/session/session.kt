package com.cedica.cedica.core.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepository(
    private val dataStore: DataStore<Preferences>
){
    private companion object {
        val USER_ID = intPreferencesKey("USER_ID")
    }

    suspend fun saveUserID(userID: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userID
        }
    }

    val userID: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }
}

// Definición del data store
const val DATA_STORE_NAME = "SESSION"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)