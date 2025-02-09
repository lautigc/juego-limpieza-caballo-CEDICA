package com.cedica.cedica

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GameState(
    private var score: Int = 0,
    private var difficulty: String = "Normal",
    private var currentStage: Int = 1,
    private var attemptsLeft: Int = 3,
    private val startDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    private var startTime: LocalDateTime = LocalDateTime.now(),
    private var endTime: LocalDateTime? = null,
    private var completionTime: Duration? = null,
    private var elapsedTime: Long = 0L // Segundos transcurridos durante la partida
) {

    fun addScore(points: Int) {
        score += points
    }

    fun decreaseAttempts() {
        if (attemptsLeft > 0) attemptsLeft--
    }

    fun finishGame() {
        endTime = LocalDateTime.now()
        completionTime = Duration.between(startTime, endTime)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getFormattedCompletionTime(): String {
        val duration = completionTime ?: Duration.ofSeconds(0)
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart())
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getFormattedElapsedTime(): String {
        val duration = Duration.ofSeconds(elapsedTime)
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart())
    }

    fun updateElapsedTime() {
        elapsedTime++
    }

    fun getScore(): Int = score
    fun getDifficulty(): String = difficulty
    fun getCurrentStage(): Int = currentStage
    fun getAttemptsLeft(): Int = attemptsLeft
    fun getStartDate(): String = startDate
    fun getElapsedTime(): Long = elapsedTime
    fun getCompletionTime(): Duration? = completionTime
}