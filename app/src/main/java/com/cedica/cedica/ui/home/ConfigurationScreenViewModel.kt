package com.cedica.cedica.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.configuration.GlobalConfiguration
import com.cedica.cedica.core.configuration.GlobalConfigurationDefaults
import com.cedica.cedica.core.configuration.GlobalConfigurationState
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.core.utils.input_field.InputField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class ConfigurationScreenUiState(
    val generalVolume: InputField<Int> = InputField(
        GlobalConfigurationDefaults.GENERAL.defaultValue
    ),
    val effectsVolume: InputField<Int> = InputField(
        GlobalConfigurationDefaults.EFFECTS.defaultValue
    ),
    val musicVolume: InputField<Int> = InputField(
        GlobalConfigurationDefaults.MUSIC.defaultValue
    ),
)

class ConfigurationScreenViewModel(
    private val globalConfiguration: GlobalConfiguration,
    private val session: Session,
): ViewModel() {
    private val _uiState = MutableStateFlow(ConfigurationScreenUiState())
    val uiState: StateFlow<ConfigurationScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val configuration = globalConfiguration.getGlobalConfiguration().first()
            _uiState.value = ConfigurationScreenUiState(
                generalVolume = InputField(configuration.generalVolume),
                effectsVolume = InputField(configuration.effectsVolume),
                musicVolume = InputField(configuration.musicVolume),
            )
        }
    }

    fun saveConfiguration() {
        viewModelScope.launch {
            globalConfiguration.setGlobalConfiguration(
                GlobalConfigurationState(
                    generalVolume = _uiState.value.generalVolume.input,
                    effectsVolume = _uiState.value.effectsVolume.input,
                    musicVolume = _uiState.value.musicVolume.input,
                )
            )
        }
    }

    fun getUserID(): Long = runBlocking {
        session.getUserID().first()
    }
}
