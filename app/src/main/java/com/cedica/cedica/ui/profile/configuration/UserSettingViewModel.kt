package com.cedica.cedica.ui.profile.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.utils.input_field.InputField
import com.cedica.cedica.core.utils.input_field.NumberField
import com.cedica.cedica.data.configuration.ConfigurationConstraints
import com.cedica.cedica.data.configuration.DifficultyLevel
import com.cedica.cedica.data.configuration.VoiceType
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.data.user.LoadingUser
import kotlinx.coroutines.launch

class UserSettingViewModel(
    private val userID: Long,
    private val userRepository: UserRepository,
): ViewModel() {
    val voice: InputField<VoiceType> = InputField(ConfigurationConstraints.DEFAULT_VOICE)
    val level: InputField<DifficultyLevel> = InputField(ConfigurationConstraints.DEFAULT_DIFFICULTY)
    val time: NumberField<Int> = NumberField(
        rangeStart = ConfigurationConstraints.MIN_TIME,
        rangeEnd = ConfigurationConstraints.MAX_TIME,
        initialValue = ConfigurationConstraints.DEFAULT_TIME
    )
    val imageCount: NumberField<Int> = NumberField(
        rangeStart = ConfigurationConstraints.MIN_IMAGES,
        rangeEnd = ConfigurationConstraints.MAX_IMAGES,
        initialValue = ConfigurationConstraints.DEFAULT_IMAGES,
    )
    val tryCount: NumberField<Int> = NumberField(
        rangeStart = ConfigurationConstraints.MIN_ATTEMPTS,
        rangeEnd = ConfigurationConstraints.MAX_ATTEMPTS,
        initialValue = ConfigurationConstraints.DEFAULT_ATTEMPTS,
    )

    private var user = LoadingUser

    private val validableFields = listOf(time, imageCount, tryCount)

    init {
        viewModelScope.launch {
            user = userRepository.getByID(userID)
            voice.onChange(user.personalConfiguration.voiceType)
            level.onChange(user.personalConfiguration.difficultyLevel)
            time.onChange(user.personalConfiguration.secondsTime)
            imageCount.onChange(user.personalConfiguration.numberOfImages)
            tryCount.onChange(user.personalConfiguration.numberOfAttempts)
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
                        voiceType = voice.input,
                        difficultyLevel = level.input,
                        secondsTime = time.input,
                        numberOfImages = imageCount.input,
                        numberOfAttempts = tryCount.input,
                    )
                )
                userRepository.update(user)
            }
        }
    }
}
