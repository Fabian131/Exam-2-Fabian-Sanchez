package com.moviles.paninisupport

import androidx.compose.runtime.Composable
import com.moviles.paninisupport.navigation.AppNavHost
import com.moviles.paninisupport.ui.theme.PaniniSupportTheme

@Composable
fun PaniniSupportApp() {
    PaniniSupportTheme {
        AppNavHost()
    }
}