

import com.bosta.games.domain.repository.GamesRepository
import com.example.bosta_android_task.domain.model.Game
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching games list
 * Encapsulates business logic for getting games
 */
class GetGamesUseCase @Inject constructor(
    private val repository: GamesRepository
) {
    /**
     * Execute the use case
     * @param page Page number
     * @param pageSize Number of items per page
     * @param genreId Optional genre filter
     * @param searchQuery Optional search query
     */
    operator fun invoke(
        page: Int,
        pageSize: Int = 20,
        genreId: Int? = null,
        searchQuery: String? = null
    ): Flow<Result<List<Game>>> {
        return repository.getGames(
            page = page,
            pageSize = pageSize,
            genreId = genreId,
            searchQuery = searchQuery
        )
    }
}
