package com.example.bosta_android_task.presentation.games
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bosta.games.presentation.details.GameDetailsScreen
import com.bosta.games.presentation.games.GamesScreen

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
            GamesScreen(
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
