package com.destos.ares.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.destos.ares.R
import com.destos.ares.components.CyberBackground
import com.destos.ares.navigation.Routes
import com.destos.ares.theme.CyberColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startFrame by remember { mutableStateOf(false) }
    var startCircuits by remember { mutableStateOf(false) }
    var startEye by remember { mutableStateOf(false) }

    val frameScale by animateFloatAsState(
        targetValue = if (startFrame) 1f else 0.3f,
        animationSpec = tween(1100, easing = FastOutSlowInEasing), label = "f1"
    )
    val frameAlpha by animateFloatAsState(
        targetValue = if (startFrame) 1f else 0f,
        animationSpec = tween(900), label = "f2"
    )
    val circuitAlpha by animateFloatAsState(
        targetValue = if (startCircuits) 0.85f else 0f,
        animationSpec = tween(1100, easing = LinearEasing), label = "c1"
    )
    val eyeScale by animateFloatAsState(
        targetValue = if (startEye) 1f else 1.6f,
        animationSpec = tween(900, easing = FastOutSlowInEasing), label = "e1"
    )
    val eyeAlpha by animateFloatAsState(
        targetValue = if (startEye) 1f else 0f,
        animationSpec = tween(700), label = "e2"
    )

    LaunchedEffect(Unit) {
        startFrame = true
        delay(1100)
        startCircuits = true
        delay(1100)
        startEye = true
        delay(1300)
        navController.navigate(Routes.SELECTION) {
            popUpTo(Routes.SPLASH) { inclusive = true }
        }
    }

    CyberBackground {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Dest-OS Ares",
                modifier = Modifier
                    .size(300.dp)
                    .scale(frameScale)
                    .alpha(frameAlpha),
                contentScale = ContentScale.Fit
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .alpha(circuitAlpha),
                contentScale = ContentScale.Fit
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(160.dp)
                    .scale(eyeScale)
                    .alpha(eyeAlpha),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Geliştirici: İbrahim Halil Ezen",
                color = CyberColors.LightBlue,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .alpha(circuitAlpha)
            )
        }
    }
}
