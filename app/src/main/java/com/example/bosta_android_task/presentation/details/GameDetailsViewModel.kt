package com.bosta.games.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosta.games.domain.model.GameDetails
import com.bosta.games.domain.usecase.GetGameDetailsUseCase
import com.bosta.games.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Game Details Screen
 * Handles loading detailed information for a specific game
 */
@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val getGameDetailsUseCase: GetGameDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow<UiState<GameDetails>>(UiState.Loading)
    val uiState: StateFlow<UiState<GameDetails>> = _uiState.asStateFlow()
    
    // Game ID from navigation arguments
    private val gameId: Int = checkNotNull(savedStateHandle["gameId"]) {
        "gameId is required"
    }
    
    init {
        loadGameDetails()
    }
    
    /**
     * Load game details from API
     */
    private fun loadGameDetails() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            getGameDetailsUseCase(gameId)
                .catch { exception ->
                    _uiState.value = UiState.Error(exception)
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { gameDetails ->
                            _uiState.value = UiState.Success(gameDetails)
                        },
                        onFailure = { exception ->
                            _uiState.value = UiState.Error(exception)
                        }
                    )
                }
        }
    }
    
    /**
     * Retry loading after error
     */
    fun retry() {
        loadGameDetails()
    }
}
