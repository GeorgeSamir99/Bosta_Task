
package com.example.bosta_android_task.data.api
import com.example.bosta_android_task.data.api.model.GameDetailsDto
import com.example.bosta_android_task.data.api.model.GamesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * RAWG API Service
 * Documentation: https://api.rawg.io/docs/
 */
interface RawgApiService {
    
    /**
     * Get list of games with optional filters
     * @param apiKey API key for authentication
     * @param page Page number for pagination (starts from 1)
     * @param pageSize Number of results per page (default: 20)
     * @param genres Genre IDs separated by comma (e.g., "4,51")
     * @param search Search query for game names
     * @param ordering Sort order (e.g., "-rating", "-released", "name")
     */
    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("genres") genres: String? = null,
        @Query("search") search: String? = null,
        @Query("ordering") ordering: String? = null
    ): GamesResponse
    
    /**
     * Get detailed information for a specific game
     * @param id Game ID
     * @param apiKey API key for authentication
     */
    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") id: Int,
        @Query("key") apiKey: String
    ): GameDetailsDto
    
    companion object {
        const val BASE_URL = "https://api.rawg.io/api/"
        
        // Popular genre IDs from RAWG
        object Genres {
            const val ACTION = 4
            const val INDIE = 51
            const val ADVENTURE = 3
            const val RPG = 5
            const val STRATEGY = 10
            const val SHOOTER = 2
            const val CASUAL = 40
            const val SIMULATION = 14
            const val PUZZLE = 7
            const val ARCADE = 11
            const val PLATFORMER = 83
            const val RACING = 1
            const val SPORTS = 15
            const val FIGHTING = 6
            const val FAMILY = 19
            const val BOARD_GAMES = 28
            const val EDUCATIONAL = 34
            const val CARD = 17
        }
    }
}
