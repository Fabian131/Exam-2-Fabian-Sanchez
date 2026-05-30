package com.moviles.paninisupport.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.moviles.paninisupport.ui.screens.create.CreateTicketScreen
import com.moviles.paninisupport.ui.screens.detail.TicketDetailScreen
import com.moviles.paninisupport.ui.screens.login.LoginScreen
import com.moviles.paninisupport.ui.screens.tickets.TicketListScreen
import com.moviles.paninisupport.ui.theme.AppBackground
import com.moviles.paninisupport.util.FeatureFlags

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = AppDestinations.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestinations.TICKET_LIST) {
                        popUpTo(AppDestinations.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppDestinations.TICKET_LIST) {
            val showFab = FeatureFlags.isEnabled(FeatureFlags.ENABLE_TICKET_CREATION)

            TicketListScreen(
                onTicketClick = { ticketId ->
                    navController.navigate(AppDestinations.ticketDetailRoute(ticketId))
                },
                onCreateClick = {
                    if (showFab) {
                        navController.navigate(AppDestinations.CREATE_TICKET)
                    }
                }
            )
        }

        composable(
            route = "${AppDestinations.TICKET_DETAIL}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("id") ?: ""
            TicketDetailScreen(
                ticketId = ticketId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppDestinations.CREATE_TICKET) {
            CreateTicketScreen(
                onBackClick = { navController.popBackStack() },
                onTicketCreated = {
                    navController.popBackStack()
                }
            )
        }
    }
}