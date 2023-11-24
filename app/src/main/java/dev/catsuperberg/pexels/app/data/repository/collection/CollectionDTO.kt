package dev.catsuperberg.pexels.app.data.repository.collection

import kotlinx.serialization.Serializable

@Serializable
data class CollectionDTO(
    val id: String,
    val title: String,
    val description: String?,
    val private: Boolean,
    val mediaCount: Int,
    val photosCount: Int,
    val videosCount: Int
)

@Serializable
data class CollectionRequestDTO(
    val page: Int,
    val perPage: Int,
    val totalResults: Int,
    val nextPage: String? = null,
    val prevPage: String? = null,
    val collections: List<CollectionDTO>
)
