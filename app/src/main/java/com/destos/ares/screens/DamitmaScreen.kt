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
fun DamitmaScreen(navController: NavController) {
    val context = LocalContext.current
    val language = remember { PrefsManager.getLanguage(context) }
    val gender = remember { PrefsManager.getGender(context) }
    val tts = rememberTextToSpeech(language)

    var alkolMiktari by remember { mutableStateOf("") }
    var eldekiDerece by remember { mutableStateOf("") }
    var hedefDerece by remember { mutableStateOf("") }
    var birimFiyat by remember { mutableStateOf("") }

    var ilaveSu by remember { mutableStateOf(0.0) }
    var ilaveAnason by remember { mutableStateOf(0.0) }
    var toplamHacim by remember { mutableStateOf(0.0) }
    var buyukRaki by remember { mutableStateOf(0.0) }
    var binlikRaki by remember { mutableStateOf(0.0) }
    var piyasaDegeri by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        delay(600)
        speak(
            tts, if (language == "tr")
                "Damıtma hesaplaması. Alkol miktarını, eldeki dereceyi ve hedef dereceyi gir."
            else "Distillation. Enter amount, current and target degree."
        )
    }

    fun hesapla() {
        val alkol = parseSafeDouble(alkolMiktari)
        val eldeki = parseSafeDouble(eldekiDerece)
        val hedef = parseSafeDouble(hedefDerece)
        val fiyat = parseSafeDouble(birimFiyat) ?: 0.0

        if (alkol == null || alkol <= 0 ||
            eldeki == null || eldeki <= 0 || eldeki > 100 ||
            hedef == null || hedef <= 0 || hedef >= eldeki
        ) {
            speak(
                tts, if (language == "tr")
                    "Geçersiz giriş. Hedef derece eldeki dereceden küçük olmalı."
                else "Invalid. Target must be lower than current."
            )
            return
        }

        val netAlkol = alkol * (eldeki / 100.0)
        toplamHacim = netAlkol / (hedef / 100.0)
        ilaveSu = toplamHacim - alkol
        ilaveAnason = (toplamHacim / 1000.0) * 2.0
        buyukRaki = toplamHacim / 700.0
        binlikRaki = toplamHacim / 1000.0
        piyasaDegeri = binlikRaki * fiyat

        speak(
            tts, if (language == "tr")
                "Tamamlandı. ${"%.0f".format(ilaveSu)} mililitre su, " +
                        "${"%.2f".format(ilaveAnason)} mililitre anason ekle. " +
                        "Toplam ${"%.0f".format(toplamHacim)} mililitre."
            else "Done. Add ${"%.0f".format(ilaveSu)} ml water."
        )
    }

    val micKomut = rememberVoiceInput(language) { cmd ->
        val t = cmd.lowercase()
        val n = extractNumber(cmd)
        when {
            t.contains("hesapla") || t.contains("calculate") -> hesapla()
            n.isEmpty() -> {}
            t.contains("fiyat") || t.contains("price") -> birimFiyat = n
            t.contains("hedef") || t.contains("target") -> hedefDerece = n
            t.contains("eldeki") || t.contains("current") -> eldekiDerece = n
            t.contains("miktar") || t.contains("amount") -> alkolMiktari = n
        }
    }

    CyberBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopBarSection("HESAPLAMA MERKEZİ", "DAMITMA & HESAPLAMA", "ARES", gender)

            TabMenuRow(listOf("SUMA", "DAMITMA", "SEYRELTME", "ENERJİ"), "DAMITMA") { tab ->
                when (tab) {
                    "SUMA" -> navController.navigate(Routes.SUMA)
                    "SEYRELTME" -> navController.navigate(Routes.SEYRELTME)
                    "ENERJİ" -> navController.navigate(Routes.ENERJI)
                }
            }

            CyberSection("⬇ GİRİŞLER") {
                CyberInputField(1, "ALKOL MİKTARI", alkolMiktari, "ml", { alkolMiktari = it }, micKomut)
                CyberInputField(2, "ELDEKİ ALKOL DERECESİ", eldekiDerece, "%", { eldekiDerece = it }, micKomut)
                CyberInputField(3, "HEDEFLENEN DERECE", hedefDerece, "%", { hedefDerece = it }, micKomut)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CyberLockedBoxSmall(Icons.Default.WaterDrop, "İLAVE SU", "%.0f ml".format(ilaveSu))
                    CyberLockedBoxSmall(Icons.Default.LocalFlorist, "İLAVE ANASON", "%.2f ml".format(ilaveAnason))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CyberSection("📊 HESAPLANAN DEĞERLER") {
                CyberLockedField(Icons.Default.Science, "TOPLAM HACİM", "%.0f ml".format(toplamHacim))
                CyberLockedField(Icons.Default.Liquor, "BÜYÜK RAKI (700 ml)", "%.2f Adet".format(buyukRaki))
                CyberLockedField(Icons.Default.WineBar, "1000 ml RAKI", "%.2f Adet".format(binlikRaki))
                CyberInputField(null, "1000 ml RAKI BİRİM FİYATI", birimFiyat, "TL", { birimFiyat = it }, micKomut)
                CyberHighlightBox(Icons.Default.Paid, "PİYASA DEĞERİ", "%.2f TL".format(piyasaDegeri))
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
