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
        if (!config.voiceEnabled) {
            continuation.resume(false)
            return@suspendCoroutine
        }

        this.tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()

                // Obtener la lista de voces disponibles en el idioma del sistema
                val voices = tts?.voices?.filter { it.locale.language == Locale.getDefault().language }

                // Buscar una voz masculina o femenina
                val selectedVoice = voices?.find { voice ->
                    (config.voiceType == "Masculino" && !voice.name.contains("female", true)) ||
                            (config.voiceType == "Femenino" && voice.name.contains("female", true))
                }

                if (selectedVoice != null) {
                    tts?.setVoice(selectedVoice)
                    Log.d("TTSDebug", "Voz seleccionada: ${selectedVoice.name}")
                } else {
                    Log.d("TTSDebug", "No se encontró una voz ${config.voiceType}, se usa la predeterminada")
                }

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