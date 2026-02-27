


import com.example.bosta_android_task.data.api.model.GameDetailsDto
import com.example.bosta_android_task.data.api.model.GameDto
import com.example.bosta_android_task.domain.model.Game
import com.example.bosta_android_task.domain.model.GameDetails

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
