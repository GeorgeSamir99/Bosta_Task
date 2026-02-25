package com.example.bosta_android_task.domain.repository


import com.example.bosta_android_task.domain.model.Game
import com.example.bosta_android_task.domain.model.GameDetails
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for games data
 * This is in the domain layer to follow clean architecture
 */
interface GamesRepository {
    
    /**
     * Get paginated list of games
     * @param page Page number (starts from 1)
     * @param pageSize Number of items per page
     * @param genreId Optional genre filter
     * @param searchQuery Optional search query
     * @return Flow of Result containing list of games
     */
    fun getGames(
        page: Int,
        pageSize: Int = 20,
        genreId: Int? = null,
        searchQuery: String? = null
    ): Flow<Result<List<Game>>>
    
    /**
     * Get detailed information for a specific game
     * @param gameId Game ID
     * @return Flow of Result containing game details
     */
    fun getGameDetails(gameId: Int): Flow<Result<GameDetails>>
}
