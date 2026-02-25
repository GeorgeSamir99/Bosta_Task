



import com.bosta.games.domain.model.Game
import com.bosta.games.domain.model.GameDetails
import com.bosta.games.domain.repository.GamesRepository
import com.example.bosta_android_task.data.api.RawgApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of GamesRepository
 * Handles data fetching from API and maps to domain models
 */
class GamesRepositoryImpl @Inject constructor(
    private val apiService: RawgApiService,
    private val apiKey: String
) : GamesRepository {
    
    override fun getGames(
        page: Int,
        pageSize: Int,
        genreId: Int?,
        searchQuery: String?
    ): Flow<Result<List<Game>>> = flow {
        try {
            val response = apiService.getGames(
                apiKey = apiKey,
                page = page,
                pageSize = pageSize,
                genres = genreId?.toString(),
                search = searchQuery,
                ordering = "-rating" // Sort by rating descending
            )
            
            val games = response.results.map { it.toDomain() }
            emit(Result.success(games))
            
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override fun getGameDetails(gameId: Int): Flow<Result<GameDetails>> = flow {
        try {
            val response = apiService.getGameDetails(
                id = gameId,
                apiKey = apiKey
            )
            
            val gameDetails = response.toDomain()
            emit(Result.success(gameDetails))
            
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
