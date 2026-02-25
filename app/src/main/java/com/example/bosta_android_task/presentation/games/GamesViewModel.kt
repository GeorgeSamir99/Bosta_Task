

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosta.games.domain.model.Game
import com.bosta.games.domain.usecase.GetGamesUseCase
import com.bosta.games.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Games List Screen
 * Handles pagination, search, and filtering
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class GamesViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow<UiState<List<Game>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Game>>> = _uiState.asStateFlow()
    
    // All loaded games (for local search)
    private val _allGames = MutableStateFlow<List<Game>>(emptyList())
    
    // Current displayed games (filtered or all)
    private val _displayedGames = MutableStateFlow<List<Game>>(emptyList())
    val displayedGames: StateFlow<List<Game>> = _displayedGames.asStateFlow()
    
    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // Pagination state
    private val _isPaginating = MutableStateFlow(false)
    val isPaginating: StateFlow<Boolean> = _isPaginating.asStateFlow()
    
    private var currentPage = 1
    private var canLoadMore = true
    
    // Selected genre (null = all genres)
    private val _selectedGenreId = MutableStateFlow<Int?>(null)
    val selectedGenreId: StateFlow<Int?> = _selectedGenreId.asStateFlow()
    
    init {
        loadGames()
        observeSearchQuery()
    }
    
    /**
     * Load games from API (initial load)
     */
    fun loadGames() {
        if (_uiState.value is UiState.Loading) return
        
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            currentPage = 1
            _allGames.value = emptyList()
            
            getGamesUseCase(
                page = currentPage,
                genreId = _selectedGenreId.value
            ).catch { exception ->
                _uiState.value = UiState.Error(exception)
            }.collect { result ->
                result.fold(
                    onSuccess = { games ->
                        _allGames.value = games
                        _displayedGames.value = games
                        _uiState.value = if (games.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(games)
                        }
                        canLoadMore = games.isNotEmpty()
                    },
                    onFailure = { exception ->
                        _uiState.value = UiState.Error(exception)
                    }
                )
            }
        }
    }
    
    /**
     * Load next page (pagination)
     */
    fun loadNextPage() {
        if (!canLoadMore || _isPaginating.value || _searchQuery.value.isNotEmpty()) return
        
        viewModelScope.launch {
            _isPaginating.value = true
            currentPage++
            
            getGamesUseCase(
                page = currentPage,
                genreId = _selectedGenreId.value
            ).catch { exception ->
                _isPaginating.value = false
                currentPage--
            }.collect { result ->
                result.fold(
                    onSuccess = { newGames ->
                        if (newGames.isEmpty()) {
                            canLoadMore = false
                        } else {
                            val updatedGames = _allGames.value + newGames
                            _allGames.value = updatedGames
                            _displayedGames.value = updatedGames
                            _uiState.value = UiState.Success(updatedGames)
                        }
                        _isPaginating.value = false
                    },
                    onFailure = { exception ->
                        _isPaginating.value = false
                        currentPage--
                    }
                )
            }
        }
    }
    
    /**
     * Update search query
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    /**
     * Observe search query and filter locally
     */
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Wait 300ms after user stops typing
                .distinctUntilChanged()
                .collect { query ->
                    filterGamesLocally(query)
                }
        }
    }
    
    /**
     * Filter games locally without API call
     */
    private fun filterGamesLocally(query: String) {
        if (query.isEmpty()) {
            _displayedGames.value = _allGames.value
            _uiState.value = if (_allGames.value.isEmpty()) {
                UiState.Empty
            } else {
                UiState.Success(_allGames.value)
            }
        } else {
            val filtered = _allGames.value.filter { game ->
                game.name.contains(query, ignoreCase = true)
            }
            _displayedGames.value = filtered
            _uiState.value = if (filtered.isEmpty()) {
                UiState.Empty
            } else {
                UiState.Success(filtered)
            }
        }
    }
    
    /**
     * Select genre filter
     */
    fun selectGenre(genreId: Int?) {
        _selectedGenreId.value = genreId
        _searchQuery.value = ""
        loadGames()
    }
    
    /**
     * Retry loading after error
     */
    fun retry() {
        loadGames()
    }
    
    /**
     * Clear search
     */
    fun clearSearch() {
        _searchQuery.value = ""
    }
}
