package com.destos.ares.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.destos.ares.components.*
import com.destos.ares.navigation.Routes
import com.destos.ares.utils.*
import kotlinx.coroutines.delay

@Composable
fun EnerjiScreen(navController: NavController) {
    val context = LocalContext.current
    val language = remember { PrefsManager.getLanguage(context) }
    val gender = remember { PrefsManager.getGender(context) }
    val tts = rememberTextToSpeech(language)

    var rezistansGucu by remember { mutableStateOf("") }
    var birimFiyat by remember { mutableStateOf("") }
    var damitmaSuresi by remember { mutableStateOf("") }
    var uretilenLitre by remember { mutableStateOf("") }

    var toplamTuketim by remember { mutableStateOf(0.0) }
    var toplamFatura by remember { mutableStateOf(0.0) }
    var litreBasiEnerji by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        delay(600)
        speak(
            tts, if (language == "tr")
                "Enerji hesaplaması. Rezistans gücünü, birim fiyatı, süreyi ve litreyi gir."
            else "Energy. Enter power, unit price, time and liters."
        )
    }

    fun hesapla() {
        val guc = parseSafeDouble(rezistansGucu)
        val fiyat = parseSafeDouble(birimFiyat)
        val sure = parseSafeDouble(damitmaSuresi)
        val litre = parseSafeDouble(uretilenLitre)

        if (guc == null || guc <= 0 || fiyat == null || fiyat <= 0 ||
            sure == null || sure <= 0 || litre == null || litre <= 0
        ) {
            speak(
                tts, if (language == "tr")
