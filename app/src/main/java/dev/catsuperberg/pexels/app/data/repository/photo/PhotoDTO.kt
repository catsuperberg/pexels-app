package dev.catsuperberg.pexels.app.data.repository.photo

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SrcDTO(
    val original: String,
    val large2x: String,
)

@Polymorphic
@Serializable
sealed class Media

@Serializable
@SerialName("Photo")
data class PhotoDTO(
    val id: Int,
    val photographer: String,
    val src: SrcDTO,
    val alt: String
): Media()

@Serializable
@SerialName("Video")
data object VideoDTO : Media()

interface IMediaRequestDTO {
    val media: List<Media>
}

@Serializable
data class PhotoRequestDTO(
    val page: Int,
    val perPage: Int,
    val totalResults: Int,
    @SerialName("photos") override val media: List<PhotoDTO>
): IMediaRequestDTO

@Serializable
data class MediaCollectionRequestDTO(
    val id: String,
    val page: Int,
    val perPage: Int,
    val totalResults: Int,
    override val media: List<Media>
): IMediaRequestDTO
