package com.example.bosta_android_task.domain.repository


import com.example.bosta_android_task.domain.model.Game
import com.example.bosta_android_task.domain.model.GameDetails
import kotlinx.coroutines.flow.Flow


interface GamesRepository {
    

    fun getGames(
        page: Int,
        pageSize: Int = 20,
        genreId: Int? = null,
        searchQuery: String? = null
    ): Flow<Result<List<Game>>>
    

    fun getGameDetails(gameId: Int): Flow<Result<GameDetails>>
}
