package com.cedica.cedica.core.guestData

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cedica.cedica.data.configuration.ConfigurationConstraints
import com.cedica.cedica.data.configuration.DifficultyLevel
import com.cedica.cedica.data.configuration.PersonalConfiguration
import com.cedica.cedica.data.configuration.VoiceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.enums.enumEntries

// Datastore definition
private const val DATA_STORE_NAME = "GUEST_DATA"
val Context.guestDataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)


data class GuestDataState(
    val configuration: PersonalConfiguration = PersonalConfiguration()
)
/*
* Session object to management user session
 */
object GuestData {
    // DataStore must initialize in root UI
    private lateinit var dataStore: DataStore<Preferences>

    /*
    * Initialize Singleton object. If you don't call this method, lateinit exception will be thrown
     */
    fun init(dataStore: DataStore<Preferences>) {
        this.dataStore = dataStore
    }

    // Key-values for datastore
    private val VOICE_TYPE = stringPreferencesKey("voice_type")
    private val DIFFICULTY_LEVEL = stringPreferencesKey("difficulty_level")
    private val SECONDS_TIME = intPreferencesKey("seconds_time")
    private val NUMBER_OF_IMAGES = intPreferencesKey("number_of_images")
    private val NUMBER_OF_ATTEMPTS = intPreferencesKey("number_of_attempts")


    fun getGlobalConfiguration(): Flow<GuestDataState> {
        return this.dataStore.data.map { preferences ->
            GuestDataState(
                configuration = PersonalConfiguration(
                    voiceType = VoiceType.valueOf(
                        preferences[VOICE_TYPE] ?: ConfigurationConstraints.DEFAULT_VOICE.name
                    ),
                    difficultyLevel = DifficultyLevel.valueOf(
                        preferences[DIFFICULTY_LEVEL] ?: ConfigurationConstraints.DEFAULT_DIFFICULTY.name
                    ),
                    secondsTime = preferences[SECONDS_TIME] ?: ConfigurationConstraints.DEFAULT_TIME,
                    numberOfImages = preferences[NUMBER_OF_IMAGES] ?: ConfigurationConstraints.DEFAULT_IMAGES,
                    numberOfAttempts = preferences[NUMBER_OF_ATTEMPTS] ?: ConfigurationConstraints.DEFAULT_ATTEMPTS,
                )
            )
        }
    }

    suspend fun setGlobalConfiguration(
        configuration: PersonalConfiguration
    ) {
        this.dataStore.edit { preferences ->
            preferences[VOICE_TYPE] = configuration.voiceType.name
            preferences[DIFFICULTY_LEVEL] = configuration.difficultyLevel.name
            preferences[SECONDS_TIME] = configuration.secondsTime
            preferences[NUMBER_OF_IMAGES] = configuration.numberOfImages
            preferences[NUMBER_OF_ATTEMPTS] = configuration.numberOfAttempts
        }
    }

}