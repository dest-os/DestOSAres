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
fun SeyreltmeScreen(navController: NavController) {
    val context = LocalContext.current
    val language = remember { PrefsManager.getLanguage(context) }
    val gender = remember { PrefsManager.getGender(context) }
    val tts = rememberTextToSpeech(language)

    var eldekiDerece by remember { mutableStateOf("") }
    var istenenDerece by remember { mutableStateOf("") }
    var hacimGiris by remember { mutableStateOf("") }

    var kullanilacakAlkol by remember { mutableStateOf(0.0) }
    var ilaveSu by remember { mutableStateOf(0.0) }
    var ilaveAnason by remember { mutableStateOf(0.0) }
    var toplamHacim by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        delay(600)
        speak(
            tts, if (language == "tr")
                "Seyreltme. Eldeki dereceyi, istenen dereceyi ve hacmi gir."
            else "Dilution. Enter current degree, desired degree and volume."
        )
    }

    fun hesapla() {
        val eldeki = parseSafeDouble(eldekiDerece)
        val istenen = parseSafeDouble(istenenDerece)
        val hacim = parseSafeDouble(hacimGiris)

        if (eldeki == null || eldeki <= 0 || eldeki > 100 ||
            istenen == null || istenen <= 0 || istenen >= eldeki ||
            hacim == null || hacim <= 0
        ) {
            speak(
                tts, if (language == "tr")
                    "Geçersiz giriş. İstenen derece eldeki dereceden küçük olmalı."
                else "Invalid. Desired must be lower than current."
            )
            return
        }

        kullanilacakAlkol = hacim * (istenen / eldeki)
        ilaveSu = hacim - kullanilacakAlkol
        ilaveAnason = (hacim / 1000.0) * 2.0
        toplamHacim = kullanilacakAlkol + ilaveSu

        speak(
            tts, if (language == "tr")
                "Tamamlandı. ${"%.2f".format(kullanilacakAlkol)} mililitre alkol, " +
                        "${"%.2f".format(ilaveSu)} mililitre su kullan."
            else "Done. Use ${"%.2f".format(kullanilacakAlkol)} ml alcohol."
        )
    }

    val micKomut = rememberVoiceInput(language) { cmd ->
        val t = cmd.lowercase()
        val n = extractNumber(cmd)
        when {
            t.contains("hesapla") || t.contains("calculate") -> hesapla()
            n.isEmpty() -> {}
            t.contains("istenen") || t.contains("hedef") ||
                    t.contains("desired") || t.contains("target") -> istenenDerece = n
            t.contains("eldeki") || t.contains("current") -> eldekiDerece = n
            t.contains("hacim") || t.contains("volume") -> hacimGiris = n
        }
    }

    CyberBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopBarSection("HESAPLAMA MERKEZİ", "DAMITMA & HESAPLAMA", "ARES", gender)

            TabMenuRow(listOf("SUMA", "DAMITMA", "SEYRELTME", "ENERJİ"), "SEYRELTME") { tab ->
                when (tab) {
                    "SUMA" -> navController.navigate(Routes.SUMA)
                    "DAMITMA" -> navController.navigate(Routes.DAMITMA)
                    "ENERJİ" -> navController.navigate(Routes.ENERJI)
                }
            }

            CyberSection("⬇ GİRİŞLER") {
                CyberInputField(1, "ELDEKİ ALKOL DERECESİ", eldekiDerece, "%", { eldekiDerece = it }, micKomut)
                CyberInputField(2, "ELDE EDİLMEK İSTENEN DERECE", istenenDerece, "%", { istenenDerece = it }, micKomut)
                CyberInputField(3, "HAZIRLANACAK TOPLAM HACİM", hacimGiris, "ml", { hacimGiris = it }, micKomut)
            }

            Spacer(modifier = Modifier.height(12.dp))

            CyberSection("📊 HESAPLANAN DEĞERLER") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CyberLockedBoxSmall(Icons.Default.Liquor, "KULLANILACAK ALKOL", "%.2f ml".format(kullanilacakAlkol))
                    CyberLockedBoxSmall(Icons.Default.WaterDrop, "İLAVE SU", "%.2f ml".format(ilaveSu))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CyberLockedBoxSmall(Icons.Default.LocalFlorist, "İLAVE ANASON", "%.2f ml".format(ilaveAnason))
                    CyberLockedBoxSmall(Icons.Default.Science, "TOPLAM HACİM", "%.0f ml".format(toplamHacim))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CyberButton("» HESAPLA «", { hesapla() }, Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                CyberMicButton { micKomut() }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
