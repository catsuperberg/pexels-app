package dev.catsuperberg.pexels.app.data.model

import dev.catsuperberg.pexels.app.data.database.PhotoEntity
import dev.catsuperberg.pexels.app.data.repository.photo.Media
import dev.catsuperberg.pexels.app.data.repository.photo.PhotoDTO
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import javax.inject.Inject

class PhotoMapper @Inject constructor()  : IPhotoMapper {
    override fun map(dto: PhotoDTO): PexelsPhoto = PexelsPhoto(
        id = dto.id,
        width = dto.width,
        height = dto.height,
        photographer = dto.photographer,
        urlOriginalSize = dto.src.original,
        urlOptimizedSize = dto.src.large2x,
        alt = dto.alt.ifEmpty { null }
    )

    override fun map(dto: Media): PexelsPhoto? = when(dto) {
            is PhotoDTO -> map(dto)
            else -> null
        }

    override fun map(entity: PhotoEntity): PexelsPhoto = PexelsPhoto(
        id = entity.id,
        width = entity.width,
        height = entity.height,
        photographer = entity.photographer,
        urlOriginalSize = entity.localUriOriginal,
        urlOptimizedSize = entity.localUriOptimized,
        alt = entity.alt?.ifEmpty { null }
    )

    override fun map(
        photo: PexelsPhoto,
        localUriOriginal: String,
        localUriOptimized: String,
        bookmarked: Boolean,
        cached: Boolean,
        timeAdded: Long,
    ): PhotoEntity = PhotoEntity(
        id = photo.id,
        width = photo.width,
        height = photo.height,
        photographer = photo.photographer,
        localUriOriginal = localUriOriginal,
        localUriOptimized = localUriOptimized,
        alt = photo.alt,
        bookmarked = bookmarked,
        cached = cached,
        timeAdded = timeAdded
    )
}
