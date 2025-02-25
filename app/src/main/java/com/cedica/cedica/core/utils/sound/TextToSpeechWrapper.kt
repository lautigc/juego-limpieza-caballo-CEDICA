package com.cedica.cedica.core.utils.sound

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO: reemplazar mock por acceso a la config real

data class Config(
    val voiceEnabled: Boolean,
    val voiceType: String
)

val config = Config(voiceEnabled = true, voiceType = "Masculino")


class TextToSpeechWrapper(private val context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    suspend fun initialize(): Boolean = suspendCoroutine { continuation ->
        // TODO: Tomar la configuracion de ayuda y de tipo de voz
        // Si no tiene config de voz mantener el objeto como null
        // Establecer masculino/femenino segun la config
        if (!config.voiceEnabled) {
            continuation.resume(false)
            return@suspendCoroutine
        }

        this.tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                this.isInitialized = true
                continuation.resume(true)
            } else {
                isInitialized = false
                continuation.resume(false)
            }
        }
    }

    fun speak(text: String) {
        if(!isInitialized) {
            Log.d("TTSDebug", "TTS No inicializado todavia: o faltó inicializar o se llamó demasiado rápido")
            return
        } else {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun release() {
        this.tts?.stop()
        this.tts?.shutdown()
    }

}