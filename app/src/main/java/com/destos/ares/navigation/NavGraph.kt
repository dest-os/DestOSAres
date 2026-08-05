package com.destos.ares.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.destos.ares.screens.*

object Routes {
    const val SPLASH = "splash"
    const val SELECTION = "selection"
    const val SUMA = "suma"
    const val DAMITMA = "damitma"
    const val SEYRELTME = "seyreltme"
    const val ENERJI = "enerji"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) { SplashScreen(navController) }
        composable(Routes.SELECTION) { SelectionScreen(navController) }
        composable(Routes.SUMA) { SumaScreen(navController) }
        composable(Routes.DAMITMA) { DamitmaScreen(navController) }
        composable(Routes.SEYRELTME) { SeyreltmeScreen(navController) }
        
    }
}
