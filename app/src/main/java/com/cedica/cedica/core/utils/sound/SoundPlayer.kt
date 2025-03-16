package com.cedica.cedica.core.utils.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.cedica.cedica.R
import kotlinx.coroutines.delay

class SoundPlayer(private val context: Context) {
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    fun loadSound(key: String, resId: Int) {
        val soundId = soundPool.load(context, resId, 1)
        soundMap[key] = soundId
    }

    fun playSound(key: String) {
        val soundId = soundMap[key] ?: return
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playOnlyOneSound(key:String){
        this.stopSound()
        this.playSound(key)
    }

    fun stopSound() {
        soundPool.autoPause()
    }

    fun release() {
        soundPool.release()
    }
}