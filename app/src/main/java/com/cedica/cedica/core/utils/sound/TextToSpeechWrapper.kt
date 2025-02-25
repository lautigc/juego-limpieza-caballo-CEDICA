package com.cedica.cedica.core.utils.sound

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextToSpeechWrapper(private val context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    suspend fun initialize(): Boolean = suspendCoroutine { continuation ->
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