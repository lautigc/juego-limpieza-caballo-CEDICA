package com.cedica.cedica.core.utils.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.cedica.cedica.R
import com.cedica.cedica.ui.utils.view_models.UserViewModel
import kotlinx.coroutines.delay

class SoundPlayer(private val context: Context, private val configVolume: Int) {
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()
    private var normVolume = 0f;

    init {
        if (configVolume != 0) {
            this.normVolume = (1 / configVolume).toFloat()
        } else {
            this.normVolume = 0F
        }

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
        soundPool.play(soundId, normVolume, normVolume, 1, 0, 1.0f)
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