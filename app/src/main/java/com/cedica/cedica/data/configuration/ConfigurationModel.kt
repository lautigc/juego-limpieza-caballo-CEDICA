package com.cedica.cedica.data.configuration

enum class VoiceType {
    MALE,
    FEMALE,
    NONE
}

enum class DifficultyLevel(
    private val stringRepresentation: String,
    private val secondsTime: Int,
    private val numberOfImages: Int,
    private val numberOfAttempts: Int,
) {
    EASY("Fácil", 300, 5, 10),
    MEDIUM("Medio", 200, 3, 5),
    HARD("Difícil", 100, 2, 3),
    CUSTOM("Personalizado", 300, 0, 10) {
        override fun getSecondsTime(oldValue: Int): Int = oldValue
        override fun getNumberOfImages(oldValue: Int): Int = oldValue
        override fun getNumberOfAttempts(oldValue: Int): Int = oldValue
    };

    open fun getSecondsTime(oldValue: Int): Int = secondsTime
    open fun getNumberOfImages(oldValue: Int): Int = numberOfImages
    open fun getNumberOfAttempts(oldValue: Int): Int = numberOfAttempts

    override fun toString(): String {
        return this.stringRepresentation
    }

    companion object {
        fun toDifficultyLevel(str: String): DifficultyLevel {
            return when (str) {
                EASY.toString() -> EASY
                MEDIUM.toString() -> MEDIUM
                HARD.toString() -> HARD
                CUSTOM.toString() -> CUSTOM
                else -> throw IllegalArgumentException("Invalid difficulty level")
            }
        }
    }
}

object ConfigurationConstraints {
    const val MIN_TIME = 0
    const val MAX_TIME = 3600
    const val DEFAULT_TIME = 300

    const val MIN_IMAGES = 1
    const val MAX_IMAGES = 13
    const val DEFAULT_IMAGES = 6

    const val MIN_ATTEMPTS = 1
    const val MAX_ATTEMPTS = 100
    const val DEFAULT_ATTEMPTS = 5

    val DEFAULT_VOICE = VoiceType.FEMALE

    val DEFAULT_DIFFICULTY = DifficultyLevel.EASY
}

data class DifficultyConfiguration(
    val secondsTime: Int = ConfigurationConstraints.DEFAULT_TIME,
    val numberOfImages: Int = ConfigurationConstraints.DEFAULT_IMAGES,
    val numberOfAttempts: Int = ConfigurationConstraints.DEFAULT_ATTEMPTS,
)

data class PersonalConfiguration(
    val voiceType: VoiceType = ConfigurationConstraints.DEFAULT_VOICE,
    val difficultyLevel: DifficultyLevel = ConfigurationConstraints.DEFAULT_DIFFICULTY,
    val secondsTime: Int = ConfigurationConstraints.DEFAULT_TIME,
    val numberOfImages: Int = ConfigurationConstraints.DEFAULT_IMAGES,
    val numberOfAttempts: Int = ConfigurationConstraints.DEFAULT_ATTEMPTS,
) {
    fun setDifficulty(difficultyConfiguration: DifficultyConfiguration) = copy(
        secondsTime = difficultyConfiguration.secondsTime,
        numberOfImages = difficultyConfiguration.numberOfImages,
        numberOfAttempts = difficultyConfiguration.numberOfAttempts,
    )
}