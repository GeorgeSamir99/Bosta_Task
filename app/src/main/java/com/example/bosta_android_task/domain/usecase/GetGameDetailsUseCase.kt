package com.example.bosta_android_task.domain.usecase


import com.example.bosta_android_task.domain.model.GameDetails
import com.example.bosta_android_task.domain.repository.GamesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetGameDetailsUseCase @Inject constructor(
    private val repository: GamesRepository
) {

    operator fun invoke(gameId: Int): Flow<Result<GameDetails>> {
        return repository.getGameDetails(gameId)
    }
}
