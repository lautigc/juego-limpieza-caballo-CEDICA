package com.cedica.cedica.ui.game

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GameState(
    private var score: Int = 0,
    private var difficulty: String = "Normal",
    private var currentStage: Int = 1,
    private var amountDirtyPart: Int = 100,
    private val totalAttempts: Int = 3,
    private var attemptsLeft: Int = totalAttempts,
    private var cantSuccess: Int = 0,
    private var cantErrors: Int = 0,
    private val startDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    private var startTime: LocalDateTime = LocalDateTime.now(),
    private var endTime: LocalDateTime? = null,
    private var completionTime: Duration? = null,
    private var elapsedTime: Long = 0L,
    private var totalAvailableTime: Int,
    private var selectedTool: Int? = null,
    private var messageType: String = "selection",
    private var customMessage: String? = null
) {

    fun advanceStage(finalStage: Int): Boolean {
        if (currentStage < finalStage){
            resetStageState()
            currentStage+=1
            return true
        } else {
            finishGame()
            return false
        }
    }

    private fun resetStageState() {
        selectedTool = null
        amountDirtyPart = 100
        messageType = "selection"
        customMessage = null
    }

    private fun finishGame() {
        endTime = LocalDateTime.now()
        completionTime = Duration.between(startTime, endTime)
        messageType = "success"
        messageType = "Finalizaste el juego!"
    }

    fun addScore() {
        val maxPoints = 100
        val penalty = this.cantErrors * 5
        val minPoints = 5

        val pointsToAdd = maxPoints - penalty
        this.score += if (pointsToAdd > 0) pointsToAdd else minPoints
    }

    fun decreaseAttempts() {
        if (this.attemptsLeft > 0) this.attemptsLeft -= 1
    }

    fun resetAttempts() {
        this.attemptsLeft = this.totalAttempts
    }

    fun setSelectedTool(selectedTool: Int){
        this.selectedTool = selectedTool
    }

    fun setMessageType(messageType: String){
        this.messageType = messageType
    }

    fun setCustomMessage(customMessage: String){
        this.customMessage = customMessage
    }

    fun increaseSuccess(){
        this.cantSuccess++
    }

    fun increaseError(){
        this.cantErrors++
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getFormattedCompletionTime(): String {
        val duration = completionTime ?: Duration.ofSeconds(0)
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart())
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getFormattedElapsedTime(): String {
        val duration = Duration.ofSeconds(elapsedTime)
        return this.formatDurationToString(duration)
    }

    fun updateElapsedTime() {
        elapsedTime++
    }

    private fun getRemainingTime(): Int {
        val remainingTime = totalAvailableTime - elapsedTime.toInt()
        return if (remainingTime > 0) remainingTime else 0
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getFormattedRemainingTime(): String {
        val duration = Duration.ofSeconds(getRemainingTime().toLong())
        return this.formatDurationToString(duration)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun formatDurationToString(duration: Duration): String {
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart())
    }

    fun getScore(): Int = score
    fun getDifficulty(): String = difficulty
    fun getCurrentStage(): Int = currentStage
    fun getAttemptsLeft(): Int = attemptsLeft
    fun getStartDate(): String = startDate
    fun getElapsedTime(): Long = elapsedTime
    fun getCompletionTime(): Duration? = completionTime
    fun getSelectedTool(): Int? = selectedTool
    fun getMessageType(): String = messageType
    fun getCustomMessage(): String? = customMessage
    fun getCantErrors(): Int = cantErrors
    fun getCantSuccess(): Int = cantSuccess
}