package com.example.bosta_android_task.presentation.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bosta_android_task.presentation.details.GameDetailsScreen

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    object Games : Screen("games")
    object GameDetails : Screen("game_details/{gameId}") {
        fun createRoute(gameId: Int) = "game_details/$gameId"
    }
}

/**
 * App navigation graph
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Games.route
    ) {
        // Games list screen
        composable(route = Screen.Games.route) {
            _root_ide_package_.com.example.bosta_android_task.presentation.games.GamesScreen(
                onGameClick = { gameId ->
                    navController.navigate(Screen.GameDetails.createRoute(gameId))
                }
            )
        }
        
        // Game details screen
        composable(
            route = Screen.GameDetails.route,
            arguments = listOf(
                navArgument("gameId") {
                    type = NavType.IntType
                }
            )
        ) {
            GameDetailsScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}
