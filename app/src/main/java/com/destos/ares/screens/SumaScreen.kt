package com.destos.ares.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.destos.ares.components.*
import com.destos.ares.navigation.Routes
import com.destos.ares.theme.CyberColors
import com.destos.ares.theme.CyberTextStyle
import com.destos.ares.utils.*
import kotlinx.coroutines.delay

@Composable
fun SumaScreen(navController: NavController) {
    val context = LocalContext.current
    val language = remember { PrefsManager.getLanguage(context) }
    val gender = remember { PrefsManager.getGender(context) }
    val tts = rememberTextToSpeech(language)

    var hamAlkol by remember { mutableStateOf("") }
    var olculenDerece by remember { mutableStateOf("") }
    var olculenSicaklik by remember { mutableStateOf("") }

    var duzeltme by remember { mutableStateOf(0.0) }
    var gercekDerece by remember { mutableStateOf(0.0) }
    var netAlkol by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        delay(600)
        speak(
            tts, if (language == "tr")
                "Suma hesaplaması. Ham alkol litresini, ölçülen dereceyi ve sıcaklığı gir, sonra hesapla de."
            else "Suma calculation. Enter volume, degree and temperature."
        )
    }

    fun hesapla() {
        val ham = parseSafeDouble(hamAlkol)
        val derece = parseSafeDouble(olculenDerece)
        val sicaklik = parseSafeDouble(olculenSicaklik)

        if (ham == null || ham <= 0 ||
            derece == null || derece <= 0 || derece > 100 ||
            sicaklik == null || sicaklik < -20 || sicaklik > 50
        ) {
            speak(
                tts, if (language == "tr") "Geçersiz giriş. Değerleri kontrol et."
                else "Invalid input. Check values."
            )
            return
        }

        // TAM OIML TABLOSU (interpolasyonlu)
        duzeltme = OimlTable.correction(derece, sicaklik)
        gercekDerece = (derece + duzeltme).coerceIn(0.0, 100.0)
        netAlkol = ham * (gercekDerece / 100.0)

        speak(
            tts, if (language == "tr")
                "Hesaplama tamamlandı. Gerçek derece yüzde ${"%.2f".format(gercekDerece)}. " +
                        "Net alkol ${"%.0f".format(netAlkol)} mililitre. " +
                        "Göbek ${"%.0f".format(netAlkol * 0.65)} mililitre."
            else "Done. Real degree ${"%.2f".format(gercekDerece)} percent."
        )
    }

    val micKomut = rememberVoiceInput(language) { cmd ->
        val t = cmd.lowercase()
        val n = extractNumber(cmd)
        when {
            t.contains("hesapla") || t.contains("calculate") -> hesapla()
            n.isEmpty() -> {}
            t.contains("sıcaklık") || t.contains("sicaklik") || t.contains("temp") ->
                olculenSicaklik = n
            t.contains("derece") || t.contains("degree") -> olculenDerece = n
            t.contains("ham") || t.contains("litre") || t.contains("volume") ->
                hamAlkol = n
        }
    }

    CyberBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopBarSection("HESAPLAMA MERKEZİ", "DAMITMA & HESAPLAMA", "ARES", gender)

            TabMenuRow(listOf("SUMA", "DAMITMA", "SEYRELTME", "ENERJİ"), "SUMA") { tab ->
                when (tab) {
                    "DAMITMA" -> navController.navigate(Routes.DAMITMA)
                    "SEYRELTME" -> navController.navigate(Routes.SEYRELTME)
                    "ENERJİ" -> navController.navigate(Routes.ENERJI)
                }
            }

            CyberSection("⬇ GİRİŞLER") {
                CyberInputField(1, "HAM ALKOL LİTRESİ", hamAlkol, "ml", { hamAlkol = it }, micKomut)
                CyberInputField(2, "ÖLÇÜLEN ALKOL DERECESİ", olculenDerece, "%", { olculenDerece = it }, micKomut)
                CyberInputField(3, "ÖLÇÜLEN SICAKLIK", olculenSicaklik, "°C", { olculenSicaklik = it }, micKomut)
            }

            Spacer(modifier = Modifier.height(12.dp))

            CyberSection("📊 HESAPLANAN DEĞERLER") {
                CyberLockedField(Icons.Default.Thermostat, "Sıcaklık Düzeltmesi:",
                    "%.2f".format(kotlin.math.abs(duzeltme)))
                CyberLockedField(Icons.Default.CheckCircle, "Gerçek Alkol Derecesi:",
                    "%%%.2f".format(gercekDerece))
                CyberLockedField(Icons.Default.WaterDrop, "Net Alkol Miktarı:",
                    "%.0f ml".format(netAlkol))
            }

            Spacer(modifier = Modifier.height(12.dp))

            CyberSection("⚗ FRAKSİYON DAĞILIMI") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FraksiyonKutu("ÇÖP", "(%3)", netAlkol * 0.03)
                    FraksiyonKutu("BAŞ", "(%17)", netAlkol * 0.17)
                    FraksiyonKutu("GÖBEK", "(%65)", netAlkol * 0.65)
                    FraksiyonKutu("KUYRUK", "(%15)", netAlkol * 0.15)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("TOPLAM:  ", style = CyberTextStyle.Label)
                    Text("%.0f ml".format(netAlkol), style = CyberTextStyle.Value, fontSize = 22.sp)
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

@Composable
private fun FraksiyonKutu(ad: String, yuzde: String, deger: Double) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .border(1.dp, CyberColors.NeonBlue, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .width(78.dp)
    ) {
        Text(ad, style = CyberTextStyle.Label, fontSize = 12.sp)
        Text(yuzde, color = CyberColors.LightBlue, fontSize = 10.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("%.0f".format(deger), style = CyberTextStyle.Value, fontSize = 15.sp)
        Text("ml", color = CyberColors.LightBlue, fontSize = 10.sp)
        Icon(Icons.Default.Lock, null, tint = CyberColors.LockedBlue, modifier = Modifier.size(12.dp))
    }
}
