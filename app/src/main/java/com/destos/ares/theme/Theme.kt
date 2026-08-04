package com.destos.ares.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object CyberColors {
    val Background = Color(0xFF000000)
    val NeonBlue = Color(0xFF00D4FF)
    val NeonBlueDim = Color(0xFF0A4A5E)
    val Cyan = Color(0xFF00FFFF)
    val LightBlue = Color(0xFF7FD4FF)
    val TextWhite = Color(0xFFE8F4FF)
    val LockedBlue = Color(0xFF3A6EA5)
    val BorderDim = Color(0xFF0E3A4A)
}

object CyberTextStyle {
    val Title = TextStyle(
        color = CyberColors.TextWhite,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif
    )
    val Subtitle = TextStyle(
        color = CyberColors.Cyan,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    )
    val Label = TextStyle(
        color = CyberColors.TextWhite,
        fontSize = 14.sp
    )
    val Value = TextStyle(
        color = CyberColors.Cyan,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    val SectionHeader = TextStyle(
        color = CyberColors.Cyan,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
    )
}
