package com.example.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class VoiceManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false
    var isVoiceEnabled: Boolean = true

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("vi", "VN"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("VoiceManager", "Vietnamese TTS not supported, falling back to default")
                tts?.setLanguage(Locale.getDefault())
            }
            // Child-friendly voice settings: gentle pitch and slightly slower speed
            tts?.setPitch(1.15f) // Slightly higher, friendly voice like an owl mascot
            tts?.setSpeechRate(0.9f) // Slower for 1st grade listening
            isInitialized = true
        } else {
            Log.e("VoiceManager", "TTS initialization failed")
        }
    }

    fun speak(text: String) {
        if (!isVoiceEnabled || !isInitialized) return
        // Strip emoji and asterisks for cleaner pronunciation
        val cleanText = text
            .replace(Regex("""[^\p{L}\p{N}\p{P}\s]"""), "")
            .replace("*", "")
            .trim()
        if (cleanText.isNotEmpty()) {
            tts?.stop()
            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "UTTERANCE_KID_TUTOR")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
