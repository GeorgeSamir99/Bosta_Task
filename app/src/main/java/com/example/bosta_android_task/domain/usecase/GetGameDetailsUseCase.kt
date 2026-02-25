package com.example.bosta_android_task.domain.usecase


import com.example.bosta_android_task.domain.model.GameDetails
import com.example.bosta_android_task.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching game details
 * Encapsulates business logic for getting detailed game information
 */
class GetGameDetailsUseCase @Inject constructor(
    private val repository: GamesRepository
) {
    /**
     * Execute the use case
     * @param gameId Game ID to fetch details for
     */
    operator fun invoke(gameId: Int): Flow<Result<GameDetails>> {
        return repository.getGameDetails(gameId)
    }
}
