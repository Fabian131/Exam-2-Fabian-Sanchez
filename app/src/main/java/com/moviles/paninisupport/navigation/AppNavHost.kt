package com.moviles.paninisupport.navigation

import androidx.compose.foundation.layout.fillMaxSize
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
import com.moviles.paninisupport.ui.screens.featureflags.FeatureFlagsScreen
import com.moviles.paninisupport.ui.screens.login.LoginScreen
import com.moviles.paninisupport.ui.screens.tickets.TicketListScreen

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
            TicketListScreen(
                onTicketClick = { ticketId ->
                    navController.navigate(AppDestinations.ticketDetailRoute(ticketId))
                },
                onCreateClick = {
                    navController.navigate(AppDestinations.CREATE_TICKET)
                },
                onSettingsClick = {
                    navController.navigate(AppDestinations.SETTINGS)
                }
            )
        }

        composable(route = AppDestinations.SETTINGS) {
            FeatureFlagsScreen(
                onBackClick = { navController.popBackStack() }
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
