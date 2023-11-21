package dev.catsuperberg.pexels.app.data.repository.collection

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDTO(
    val id: String,
    val title: String,
    val description: String?,
    val private: Boolean,
    @SerialName(value = "media_count") val mediaCount: Int,
    @SerialName(value = "photos_count") val photosCount: Int,
    @SerialName(value = "videos_count") val videosCount: Int
)

@Serializable
data class CollectionRequestDTO(
    val page: Int,
    @SerialName(value = "per_page") val perPage: Int,
    @SerialName(value = "total_results") val totalResults: Int,
    @SerialName(value = "next_page") val nextPage: String,
    @SerialName(value = "prev_page") val prevPage: String? = null,
    val collections: List<CollectionDTO>
)
