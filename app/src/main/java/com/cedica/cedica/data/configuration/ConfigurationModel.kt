package com.cedica.cedica.data.configuration

enum class VoiceType {
    MALE,
    FEMALE
}

enum class DifficultyLevel {
    EASY,
    MEDIUM,
    HARD
}

private fun minutesToSeconds(min: Int): Int {
    return min * 60
}

data class PersonalConfiguration(
    val voiceType: VoiceType = VoiceType.MALE,
    val difficultyLevel: DifficultyLevel = DifficultyLevel.EASY,
    val secondsTime: Int = minutesToSeconds(5),
    val numberOfImages: Int = 10,
    val numberOfAttempts: Int = 10,
)