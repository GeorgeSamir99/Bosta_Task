package com.example.bosta_android_task.domain.model


data class Game(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val rating: Double,
    val released: String?,
    val genres: List<String>,
    val platforms: List<String>,
    val metacriticScore: Int?
) {

    fun getFormattedRating(): String {
        return if (rating > 0.0) {
            String.format("%.1f", rating)
        } else {
            "N/A"
        }
    }
    

    fun getReleaseYear(): String? {
        return released?.take(4)
    }
}


data class GameDetails(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val rating: Double,
    val released: String?,
    val genres: List<String>,
    val platforms: List<String>,
    val metacriticScore: Int?,
    val trailerUrl: String?,
    val screenshots: List<String>
) {
    fun getFormattedRating(): String {
        return if (rating > 0.0) {
            String.format("%.1f", rating)
        } else {
            "N/A"
        }
    }
    
    fun getReleaseYear(): String? {
        return released?.take(4)
    }
    
    fun getFormattedReleaseDate(): String {
        return released ?: "Release date unknown"
    }
}
