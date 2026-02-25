package com.bosta.games.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.bosta.games.domain.model.GameDetails
import com.bosta.games.presentation.common.UiState

/**
 * Game Details Screen - Shows detailed information for a specific game
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: GameDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    LoadingContent()
                }
                
                is UiState.Success -> {
                    val gameDetails = (uiState as UiState.Success<GameDetails>).data
                    GameDetailsContent(gameDetails = gameDetails)
                }
                
                is UiState.Error -> {
                    ErrorContent(
                        message = (uiState as UiState.Error).message,
                        onRetry = viewModel::retry
                    )
                }
                
                else -> {
                    // Should not happen
                }
            }
        }
    }
}

/**
 * Game details content
 */
@Composable
fun GameDetailsContent(gameDetails: GameDetails) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Game header image
        item {
            AsyncImage(
                model = gameDetails.imageUrl,
                contentDescription = gameDetails.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        
        // Game name
        item {
            Text(
                text = gameDetails.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        // Rating and release date
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoChip(
                    label = "Rating",
                    value = "⭐ ${gameDetails.getFormattedRating()}"
                )
                
                if (gameDetails.metacriticScore != null) {
                    InfoChip(
                        label = "Metacritic",
                        value = gameDetails.metacriticScore.toString()
                    )
                }
            }
        }
        
        // Release date
        item {
            InfoSection(
                title = "Release Date",
                content = gameDetails.getFormattedReleaseDate()
            )
        }
        
        // Genres
        if (gameDetails.genres.isNotEmpty()) {
            item {
                InfoSection(
                    title = "Genres",
                    content = gameDetails.genres.joinToString(", ")
                )
            }
        }
        
        // Platforms
        if (gameDetails.platforms.isNotEmpty()) {
            item {
                InfoSection(
                    title = "Platforms",
                    content = gameDetails.platforms.joinToString(", ")
                )
            }
        }
        
        // Description
        if (!gameDetails.description.isNullOrEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = gameDetails.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Screenshots
        if (gameDetails.screenshots.isNotEmpty()) {
            item {
                Column {
                    Text(
                        text = "Screenshots",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(gameDetails.screenshots) { screenshot ->
                            AsyncImage(
                                model = screenshot,
                                contentDescription = "Screenshot",
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(120.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Info chip component
 */
@Composable
fun InfoChip(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Info section component
 */
@Composable
fun InfoSection(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
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
                text = "Loading game details...",
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
