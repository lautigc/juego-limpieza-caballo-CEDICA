package com.cedica.cedica.ui.profile.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.guestData.GuestData
import com.cedica.cedica.core.utils.input_field.InputField
import com.cedica.cedica.core.utils.input_field.NumberField
import com.cedica.cedica.data.configuration.ConfigurationConstraints
import com.cedica.cedica.data.configuration.DifficultyLevel
import com.cedica.cedica.data.configuration.VoiceType
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.data.user.GuestUser
import com.cedica.cedica.data.user.LoadingUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class UserSettingUiState(
    val voice: InputField<VoiceType> = InputField(ConfigurationConstraints.DEFAULT_VOICE),
    val level: InputField<DifficultyLevel> = InputField(ConfigurationConstraints.DEFAULT_DIFFICULTY),
    val time: NumberField<Int> = NumberField(
        rangeStart = ConfigurationConstraints.MIN_TIME,
        rangeEnd = ConfigurationConstraints.MAX_TIME,
        initialValue = ConfigurationConstraints.DEFAULT_TIME
    ),
    val imageCount: NumberField<Int> = NumberField(
        rangeStart = ConfigurationConstraints.MIN_IMAGES,
        rangeEnd = ConfigurationConstraints.MAX_IMAGES,
        initialValue = ConfigurationConstraints.DEFAULT_IMAGES,
    ),
    val tryCount: NumberField<Int> = NumberField(
        rangeStart = ConfigurationConstraints.MIN_ATTEMPTS,
        rangeEnd = ConfigurationConstraints.MAX_ATTEMPTS,
        initialValue = ConfigurationConstraints.DEFAULT_ATTEMPTS,
    )
)

class UserSettingViewModel(
    private val userID: Long,
    private val userRepository: UserRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(UserSettingUiState())
    val uiState: StateFlow<UserSettingUiState> = _uiState.asStateFlow()

    private var user = LoadingUser

    private val validableFields = listOf(_uiState.value.time, _uiState.value.imageCount, _uiState.value.tryCount)

    init {
        viewModelScope.launch {
            user = if (GuestUser.id == userID)
                GuestUser.copy(
                    personalConfiguration = GuestData.getGlobalConfiguration().first().configuration
                )

            else userRepository.getByID(userID)
            _uiState.value.voice.onChange(user.personalConfiguration.voiceType)
            _uiState.value.level.onChange(user.personalConfiguration.difficultyLevel)
            _uiState.value.time.onChange(user.personalConfiguration.secondsTime)
            _uiState.value.imageCount.onChange(user.personalConfiguration.numberOfImages)
            _uiState.value.tryCount.onChange(user.personalConfiguration.numberOfAttempts)
        }
    }

    fun formIsValid(): Boolean {
        return validableFields.all { it.inputIsValid() }
    }

    fun onSave() {
        viewModelScope.launch {
            if (formIsValid()) {
                user = user.copy(
                    personalConfiguration = user.personalConfiguration.copy(
                        voiceType = _uiState.value.voice.input,
                        difficultyLevel = _uiState.value.level.input,
                        secondsTime = _uiState.value.time.input,
                        numberOfImages = _uiState.value.imageCount.input,
                        numberOfAttempts = _uiState.value.tryCount.input,
                    )
                )
                if (GuestUser.id == userID)
                    GuestData.setGlobalConfiguration(user.personalConfiguration)
                else
                    userRepository.update(user)
            }
        }
    }
}
