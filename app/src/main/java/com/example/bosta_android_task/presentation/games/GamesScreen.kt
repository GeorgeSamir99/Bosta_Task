package com.example.bosta_android_task.presentation.games

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import coil.compose.AsyncImage
import com.example.bosta_android_task.domain.model.Game


/**
 * Games List Screen - Main screen showing list of games
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    onGameClick: (Int) -> Unit,
    viewModel: GamesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val displayedGames by viewModel.displayedGames.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isPaginating by viewModel.isPaginating.collectAsState()
    
    val listState = rememberLazyListState()
    
    // Detect when user scrolls near the end for pagination
    LaunchedEffect(listState) {
        snapshotFlow { 
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index 
        }.collect { lastVisibleIndex ->
            if (lastVisibleIndex != null && 
                lastVisibleIndex >= displayedGames.size - 3 &&
                !isPaginating &&
                searchQuery.isEmpty()) {
                viewModel.loadNextPage()
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Video Games Browser") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                onClearClick = viewModel::clearSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            
            // Content based on state
            when (uiState) {
                is UiState.Idle -> {
                    // Should not happen
                }
                
                is UiState.Loading -> {
                    LoadingContent()
                }
                
                is UiState.Success -> {
                    GamesList(
                        games = displayedGames,
                        onGameClick = onGameClick,
                        listState = listState,
                        isPaginating = isPaginating
                    )
                }
                
                is UiState.Error -> {
                    ErrorContent(
                        message = (uiState as UiState.Error).message,
                        onRetry = viewModel::retry
                    )
                }
                
                is UiState.Empty -> {
                    EmptyContent(searchQuery = searchQuery)
                }
            }
        }
    }
}

/**
 * Search bar component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search games...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors()
    )
}

/**
 * Games list with pagination
 */
@Composable
fun GamesList(
    games: List<Game>,
    onGameClick: (Int) -> Unit,
    listState: LazyListState,
    isPaginating: Boolean
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = games,
            key = { it.id }
        ) { game ->
            GameItem(
                game = game,
                onClick = { onGameClick(game.id) }
            )
        }
        
        // Pagination loader at the end
        if (isPaginating) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

/**
 * Individual game item card
 */
@Composable
fun GameItem(
    game: Game,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Game image
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.name,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Game info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐ ${game.getFormattedRating()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    if (game.metacriticScore != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "MC: ${game.metacriticScore}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                
                if (game.released != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Released: ${game.getReleaseYear()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Loading state UI
 */
@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading games...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * Error state UI
 */
@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "❌ Error",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

/**
 * Empty state UI
 */
@Composable
fun EmptyContent(searchQuery: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "🎮",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (searchQuery.isNotEmpty()) {
                    "No games found for \"$searchQuery\""
                } else {
                    "No games available"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
