

package com.example.bosta_android_task.di


import com.example.bosta_android_task.data.repository.GamesRepositoryImpl
import com.example.bosta_android_task.data.api.RawgApiService
import com.example.bosta_android_task.domain.repository.GamesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    

    @Provides
    @Singleton
    fun provideGamesRepository(
        apiService: RawgApiService,
        apiKey: String
    ): GamesRepository {
        return GamesRepositoryImpl(apiService, apiKey)
    }
}
