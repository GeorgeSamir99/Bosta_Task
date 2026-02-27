package com.example.bosta_android_task.presentation.games


import com.example.bosta_android_task.domain.model.Game
import com.example.bosta_android_task.domain.usecase.GetGamesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Example Unit Test for GamesViewModel
 * 
 * To run this test:
 * 1. Make sure you have the test dependencies in build.gradle
 * 2. Right-click on the file and select "Run 'GamesViewModelTest'"
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GamesViewModelTest {
    
    // Mock dependencies
    private lateinit var getGamesUseCase: GetGamesUseCase
    private lateinit var viewModel: GamesViewModel
    
    // Test dispatcher
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        // Set main dispatcher for testing
        Dispatchers.setMain(testDispatcher)
        
        // Create mock use case
        getGamesUseCase = mockk()
    }
    
    @After
    fun tearDown() {
        // Reset main dispatcher
        Dispatchers.resetMain()
    }
    
    @Test
    fun `loadGames should emit Success state when API returns data`() = runTest {
        // Given
        val mockGames = listOf(
            Game(
                id = 1,
                name = "Test Game",
                imageUrl = "https://example.com/image.jpg",
                rating = 4.5,
                released = "2024-01-01",
                genres = listOf("Action"),
                platforms = listOf("PC"),
                metacriticScore = 85
            )
        )
        
        // Mock the use case to return success
        coEvery { 
            getGamesUseCase(page = 1, pageSize = 20, genreId = null, searchQuery = null) 
        } returns flowOf(Result.success(mockGames))
        
        // When
        viewModel = GamesViewModel(getGamesUseCase)
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(mockGames, (state as UiState.Success).data)
    }
    
    @Test
    fun `loadGames should emit Error state when API fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        
        // Mock the use case to return failure
        coEvery { 
            getGamesUseCase(page = 1, pageSize = 20, genreId = null, searchQuery = null) 
        } returns flowOf(Result.failure(exception))
        
        // When
        viewModel = GamesViewModel(getGamesUseCase)
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("Network error", (state as UiState.Error).message)
    }
    
    @Test
    fun `loadGames should emit Empty state when API returns empty list`() = runTest {
        // Given
        val emptyList = emptyList<Game>()
        
        // Mock the use case to return empty list
        coEvery { 
            getGamesUseCase(page = 1, pageSize = 20, genreId = null, searchQuery = null) 
        } returns flowOf(Result.success(emptyList))
        
        // When
        viewModel = GamesViewModel(getGamesUseCase)
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Empty)
    }
    
    @Test
    fun `onSearchQueryChanged should filter games locally`() = runTest {
        // Given
        val mockGames = listOf(
            Game(1, "Grand Theft Auto V", null, 4.5, null, emptyList(), emptyList(), null),
            Game(2, "The Witcher 3", null, 4.8, null, emptyList(), emptyList(), null),
            Game(3, "Grand Theft Auto IV", null, 4.2, null, emptyList(), emptyList(), null)
        )
        
        coEvery { 
            getGamesUseCase(page = 1, pageSize = 20, genreId = null, searchQuery = null) 
        } returns flowOf(Result.success(mockGames))
        
        viewModel = GamesViewModel(getGamesUseCase)
        advanceUntilIdle()
        
        // When
        viewModel.onSearchQueryChanged("Witcher")
        advanceUntilIdle()
        
        // Then
        val displayedGames = viewModel.displayedGames.value
        assertEquals(1, displayedGames.size)
        assertEquals("The Witcher 3", displayedGames[0].name)
    }
}
