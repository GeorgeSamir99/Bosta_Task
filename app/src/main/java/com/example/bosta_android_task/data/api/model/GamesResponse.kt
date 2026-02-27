package com.example.bosta_android_task.data.api.model
import com.google.gson.annotations.SerializedName


data class GamesResponse(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("previous")
    val previous: String?,
    
    @SerializedName("results")
    val results: List<GameDto>
)


data class GameDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("background_image")
    val backgroundImage: String?,
    
    @SerializedName("rating")
    val rating: Double,
    
    @SerializedName("released")
    val released: String?,
    
    @SerializedName("genres")
    val genres: List<GenreDto>?,
    
    @SerializedName("platforms")
    val platforms: List<PlatformWrapper>?,
    
    @SerializedName("metacritic")
    val metacritic: Int?
)

data class GenreDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String
)

data class PlatformWrapper(
    @SerializedName("platform")
    val platform: PlatformDto
)

data class PlatformDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String
)
