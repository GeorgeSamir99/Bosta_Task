

import com.bosta.games.domain.model.Game
import com.bosta.games.domain.model.GameDetails
import com.example.bosta_android_task.data.api.model.GameDetailsDto
import com.example.bosta_android_task.data.api.model.GameDto



/**
 * Extension function to map GameDto to domain Game model
 */
fun GameDto.toDomain(): Game {
    return Game(
        id = id,
        name = name,
        imageUrl = backgroundImage,
        rating = rating,
        released = released,
        genres = genres?.map { it.name } ?: emptyList(),
        platforms = platforms?.map { it.platform.name } ?: emptyList(),
        metacriticScore = metacritic
    )
}

/**
 * Extension function to map GameDetailsDto to domain GameDetails model
 */
fun GameDetailsDto.toDomain(): GameDetails {
    // Try to get the best quality video URL
    val trailerUrl = clip?.clips?.values?.firstOrNull()?.let {
        it.qualityMax ?: it.quality640 ?: it.quality320
    }
    
    return GameDetails(
        id = id,
        name = name,
        description = descriptionRaw,
        imageUrl = backgroundImage,
        rating = rating,
        released = released,
        genres = genres?.map { it.name } ?: emptyList(),
        platforms = platforms?.map { it.platform.name } ?: emptyList(),
        metacriticScore = metacritic,
        trailerUrl = trailerUrl,
        screenshots = screenshots?.map { it.image } ?: emptyList()
    )
}
