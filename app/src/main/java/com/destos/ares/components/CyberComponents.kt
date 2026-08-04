package com.destos.ares.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.destos.ares.R
import com.destos.ares.theme.CyberColors
import com.destos.ares.theme.CyberTextStyle

@Composable
fun CyberBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background),
        content = content
    )
}

@Composable
fun CyberSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, CyberColors.BorderDim, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(title, style = CyberTextStyle.SectionHeader)
        Spacer(modifier = Modifier.height(10.dp))
        content()
    }
}

@Composable
fun CyberInputField(
    number: Int?,
    label: String,
    value: String,
    unit: String,
    onValueChange: (String) -> Unit,
    onMicClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, CyberColors.NeonBlue, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (number != null) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .border(1.dp, CyberColors.NeonBlue, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(number.toString(), color = CyberColors.Cyan, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = CyberTextStyle.Label, fontSize = 12.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = value,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it == '.' || it == ',' }) {
                            onValueChange(input)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    textStyle = CyberTextStyle.Value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = CyberColors.Cyan
                    ),
                    singleLine = true
                )
                Text(unit, color = CyberColors.LightBlue, fontSize = 14.sp)
            }
        }
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Sesli giriş",
            tint = CyberColors.NeonBlue,
            modifier = Modifier
                .size(28.dp)
                .clickable { onMicClick() }
        )
    }
}

@Composable
fun CyberLockedField(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, CyberColors.BorderDim, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = CyberColors.NeonBlue, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = CyberTextStyle.Label, modifier = Modifier.weight(1f))
        Text(value, style = CyberTextStyle.Value)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.Lock, null, tint = CyberColors.LockedBlue, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun CyberLockedBoxSmall(icon: ImageVector, label: String, value: String) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, CyberColors.NeonBlue, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = CyberColors.NeonBlue, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, style = CyberTextStyle.Label, fontSize = 11.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = CyberTextStyle.Value, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Icon(Icons.Default.Lock, null, tint = CyberColors.LockedBlue, modifier = Modifier.size(14.dp))
    }
}

@Composable
fun CyberHighlightBox(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(2.dp, CyberColors.NeonBlue, RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = CyberColors.NeonBlue, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = CyberTextStyle.Label)
            Text(value, style = CyberTextStyle.Value, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.Lock, null, tint = CyberColors.LockedBlue, modifier = Modifier.size(22.dp))
    }
}

@Composable
fun CyberButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(2.dp, CyberColors.NeonBlue, RoundedCornerShape(10.dp))
            .background(CyberColors.NeonBlueDim.copy(alpha = 0.3f))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = CyberTextStyle.Title, fontSize = 18.sp)
    }
}

@Composable
fun CyberMicButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .border(3.dp, CyberColors.NeonBlue, CircleShape)
            .background(CyberColors.NeonBlueDim.copy(alpha = 0.4f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Mic, "Sesli komut",
            tint = CyberColors.TextWhite,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun TabMenuRow(tabs: List<String>, activeTab: String, onTabSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEach { tab ->
            val isActive = tab == activeTab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = if (isActive) 2.dp else 1.dp,
                        color = if (isActive) CyberColors.Cyan else CyberColors.BorderDim,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .background(
                        if (isActive) CyberColors.NeonBlueDim.copy(alpha = 0.5f)
                        else Color.Transparent
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (isActive) "» $tab «" else tab,
                    color = if (isActive) CyberColors.Cyan else CyberColors.LightBlue,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TopBarSection(title: String, subtitle: String, characterName: String, gender: String) {
    val avatarRes = if (gender == "male") R.drawable.ares_male else R.drawable.ares_female

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Dest-OS",
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = CyberTextStyle.Title, fontSize = 17.sp)
            Text(subtitle, style = CyberTextStyle.Subtitle, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = characterName,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, CyberColors.NeonBlue, CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(characterName, color = CyberColors.Cyan, fontSize = 12.sp)
        }
    }
}
