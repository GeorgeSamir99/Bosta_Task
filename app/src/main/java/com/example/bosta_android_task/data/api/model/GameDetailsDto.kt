package com.example.bosta_android_task.data.api.model
import com.google.gson.annotations.SerializedName

/**
 * Detailed game response from RAWG API
 */
data class GameDetailsDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description_raw")
    val descriptionRaw: String?,
    
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
    val metacritic: Int?,
    
    @SerializedName("clip")
    val clip: ClipDto?,
    
    @SerializedName("short_screenshots")
    val screenshots: List<ScreenshotDto>?
)

data class ClipDto(
    @SerializedName("clip")
    val clip: String?,
    
    @SerializedName("clips")
    val clips: Map<String, VideoClipDto>?
)

data class VideoClipDto(
    @SerializedName("320")
    val quality320: String?,
    
    @SerializedName("640")
    val quality640: String?,
    
    @SerializedName("max")
    val qualityMax: String?
)

data class ScreenshotDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("image")
    val image: String
)
