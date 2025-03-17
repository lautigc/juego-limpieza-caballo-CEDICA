package com.cedica.cedica.core.configuration

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Datastore definition
private const val DATA_STORE_NAME = "GLOBAL_CONFIGURATION"
val Context.globalConfigurationStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

enum class GlobalConfigurationDefaults(val defaultValue: Int) {
    GENERAL(100),
    MUSIC(100),
    EFFECTS(100),
}


data class GlobalConfigurationState(
    val generalVolume: Int = GlobalConfigurationDefaults.GENERAL.defaultValue,
    val musicVolume: Int = GlobalConfigurationDefaults.MUSIC.defaultValue,
    val effectsVolume: Int = GlobalConfigurationDefaults.EFFECTS.defaultValue,
)

/*
* Session object to management user session
 */
object GlobalConfiguration {
    // DataStore must initialize in root UI
    private lateinit var dataStore: DataStore<Preferences>

    /*
    * Initialize Singleton object. If you don't call this method, lateinit exception will be thrown
     */
    fun init(dataStore: DataStore<Preferences>) {
        this.dataStore = dataStore
    }

    // Key-values for datastore
    private val GENERAL_VOLUME = intPreferencesKey("GENERAL_VOLUME")
    private val MUSIC_VOLUME = intPreferencesKey("MUSIC_VOLUME")
    private val EFFECTS_VOLUME = intPreferencesKey("EFFECTS_VOLUME")

    fun getGlobalConfiguration(): Flow<GlobalConfigurationState> {
        return this.dataStore.data.map { preferences ->
            GlobalConfigurationState(
                generalVolume = preferences[GENERAL_VOLUME] ?: GlobalConfigurationDefaults.GENERAL.defaultValue,
                musicVolume = preferences[MUSIC_VOLUME] ?: GlobalConfigurationDefaults.MUSIC.defaultValue,
                effectsVolume = preferences[EFFECTS_VOLUME] ?: GlobalConfigurationDefaults.EFFECTS.defaultValue,
            )
        }
    }

    suspend fun setGlobalConfiguration(
        configuration: GlobalConfigurationState
    ) {
        this.dataStore.edit { preferences ->
            preferences[GENERAL_VOLUME] = configuration.generalVolume
            preferences[MUSIC_VOLUME] = configuration.musicVolume
            preferences[EFFECTS_VOLUME] = configuration.effectsVolume
        }
    }

}