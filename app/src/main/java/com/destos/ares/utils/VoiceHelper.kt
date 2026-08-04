package com.destos.ares.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.util.Locale

fun ttsLocale(lang: String): Locale = when (lang) {
    "en" -> Locale.ENGLISH
    "de" -> Locale.GERMAN
    "fr" -> Locale.FRENCH
    "es" -> Locale("es", "ES")
    "ru" -> Locale("ru", "RU")
    else -> Locale("tr", "TR")
}

@Composable
fun rememberTextToSpeech(language: String): TextToSpeech? {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(language) {
        val instance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = ttsLocale(language)
            }
        }
        tts = instance
        onDispose {
            instance.stop()
            instance.shutdown()
        }
    }
    return tts
}

fun speak(tts: TextToSpeech?, text: String) {
    try {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ares_tts")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun rememberVoiceInput(
    language: String,
    onResult: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    var startAfterPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startAfterPermission = true
    }

    LaunchedEffect(startAfterPermission) {
        if (startAfterPermission) {
            startAfterPermission = false
            startSpeechRecognition(context, language, onResult)
        }
    }

    return {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            startSpeechRecognition(context, language, onResult)
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
}

private fun startSpeechRecognition(
    context: Context,
    language: String,
    onResult: (String) -> Unit
) {
    try {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) return

        val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, ttsLocale(language))
        }

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                if (!text.isNullOrBlank()) onResult(text)
                recognizer.destroy()
            }
            override fun onError(error: Int) { recognizer.destroy() }
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        recognizer.startListening(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun parseSafeDouble(input: String): Double? {
    if (input.isBlank()) return null
    val cleaned = input.trim()
        .replace(",", ".")
        .filter { it.isDigit() || it == '.' || it == '-' }
    val parts = cleaned.split(".")
    val normalized = if (parts.size > 2) {
        parts[0] + "." + parts.drop(1).joinToString("")
    } else cleaned
    val value = normalized.toDoubleOrNull()
    return if (value != null && value.isFinite() &&
        kotlin.math.abs(value) < 1e12
    ) value else null
}

fun extractNumber(spoken: String): String {
    val normalized = spoken.lowercase()
        .replace("virgül", ".")
        .replace("nokta", ".")
        .replace("komma", ".")
        .replace("point", ".")
    val regex = Regex("""\d+\.?\d*""")
    return regex.find(normalized)?.value ?: ""
}
