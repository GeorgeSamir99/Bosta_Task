package com.example.bosta_android_task.domain.usecase
import com.example.bosta_android_task.domain.model.Game
import com.example.bosta_android_task.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetGamesUseCase @Inject constructor(
    private val repository: GamesRepository
) {

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
