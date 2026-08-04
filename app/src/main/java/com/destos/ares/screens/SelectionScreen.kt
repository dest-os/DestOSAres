package com.destos.ares.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.destos.ares.R
import com.destos.ares.components.CyberBackground
import com.destos.ares.navigation.Routes
import com.destos.ares.theme.CyberColors
import com.destos.ares.theme.CyberTextStyle
import com.destos.ares.utils.PrefsManager
import com.destos.ares.utils.rememberTextToSpeech
import com.destos.ares.utils.speak
import kotlinx.coroutines.delay

@Composable
fun SelectionScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf("tr") }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var navigated by rememberSaveable { mutableStateOf(false) }
    val tts = rememberTextToSpeech(selectedLanguage)

    val languages = listOf(
        "tr" to "TÜRKÇE", "en" to "ENGLISH", "de" to "DEUTSCH",
        "fr" to "FRANÇAIS", "es" to "ESPAÑOL", "ru" to "РУССКИЙ"
    )

    LaunchedEffect(selectedGender) {
        val gender = selectedGender
        if (gender != null && !navigated) {
            navigated = true
            PrefsManager.saveSelections(context, selectedLanguage, gender)
            speak(
                tts, when (selectedLanguage) {
                    "en" -> "Welcome. I am Ares. Calculation center is ready."
                    "de" -> "Willkommen. Ich bin Ares."
                    "fr" -> "Bienvenue. Je suis Ares."
                    "es" -> "Bienvenido. Soy Ares."
                    "ru" -> "Добро пожаловать. Я Арес."
                    else -> "Hoş geldin. Ben Ares. Hesaplama merkezi hazır."
                }
            )
            delay(1600)
            navController.navigate(Routes.SUMA) {
                popUpTo(Routes.SELECTION) { inclusive = true }
            }
        }
    }

    CyberBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Dest-OS",
                modifier = Modifier.size(110.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text("DİL SEÇİMİ", style = CyberTextStyle.Title, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))

            languages.chunked(3).forEach { rowLangs ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowLangs.forEach { (code, name) ->
                        val isSelected = selectedLanguage == code
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) CyberColors.Cyan else CyberColors.BorderDim,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedLanguage = code }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = if (isSelected) "$name »" else name,
                                color = if (isSelected) CyberColors.Cyan else CyberColors.TextWhite,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            Text("KARAKTER SEÇİMİ", style = CyberTextStyle.Title, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CharacterCard(R.drawable.ares_female, selectedGender == "female") {
                    selectedGender = "female"
                }
                CharacterCard(R.drawable.ares_male, selectedGender == "male") {
                    selectedGender = "male"
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "DEVAM ETMEK İÇİN KARAKTERİNİ SEÇ",
                color = CyberColors.LightBlue,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CharacterCard(imageRes: Int, isSelected: Boolean, onSelect: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                if (isSelected) 3.dp else 1.dp,
                if (isSelected) CyberColors.Cyan else CyberColors.BorderDim,
                RoundedCornerShape(12.dp)
            )
            .clickable { onSelect() }
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Ares",
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("ARES", style = CyberTextStyle.Subtitle)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .border(
                    1.dp,
                    if (isSelected) CyberColors.Cyan else CyberColors.NeonBlue,
                    RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 22.dp, vertical = 6.dp)
        ) {
            Text(
                text = if (isSelected) "« SEÇ »" else "SEÇ",
                color = CyberColors.Cyan,
                fontSize = 13.sp
            )
        }
    }
}
