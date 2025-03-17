package com.cedica.cedica.data.configuration

enum class VoiceType {
    MALE,
    FEMALE,
    NONE
}

enum class DifficultyLevel(private val stringRepresentation: String) {
    EASY("Fácil"),
    MEDIUM("Medio"),
    HARD("Difícil");

    override fun toString(): String {
        return this.stringRepresentation
    }

    companion object {
        fun toDifficultyLevel(str: String): DifficultyLevel {
            return when (str) {
                EASY.toString() -> EASY
                MEDIUM.toString() -> MEDIUM
                HARD.toString() -> HARD
                else -> throw IllegalArgumentException("Invalid difficulty level")
            }
        }
    }
}

object ConfigurationConstraints {
    const val MIN_TIME = 0
    const val MAX_TIME = 3600
    const val DEFAULT_TIME = 300

    const val MIN_IMAGES = 0
    const val MAX_IMAGES = 5
    const val DEFAULT_IMAGES = 0

    const val MIN_ATTEMPTS = 0
    const val MAX_ATTEMPTS = 100
    const val DEFAULT_ATTEMPTS = 10

    val DEFAULT_VOICE = VoiceType.MALE

    val DEFAULT_DIFFICULTY = DifficultyLevel.EASY
}

data class PersonalConfiguration(
    val voiceType: VoiceType = ConfigurationConstraints.DEFAULT_VOICE,
    val difficultyLevel: DifficultyLevel = ConfigurationConstraints.DEFAULT_DIFFICULTY,
    val secondsTime: Int = ConfigurationConstraints.DEFAULT_TIME,
    val numberOfImages: Int = ConfigurationConstraints.DEFAULT_IMAGES,
    val numberOfAttempts: Int = ConfigurationConstraints.DEFAULT_ATTEMPTS,
)