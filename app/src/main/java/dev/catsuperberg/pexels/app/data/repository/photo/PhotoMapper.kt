package dev.catsuperberg.pexels.app.data.repository.photo

import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import javax.inject.Inject

class PhotoMapper @Inject constructor()  : IPhotoMapper {
    override fun map(dto: PhotoDTO): PexelsPhoto =
        PexelsPhoto(
            id = dto.id,
            photographer = dto.photographer,
            urlOriginalSize = dto.src.original,
            urlOptimizedSize = dto.src.large2x,
            alt = dto.alt.ifEmpty { null }
        )

    override fun map(dto: Media): PexelsPhoto? = when(dto) {
            is PhotoDTO -> map(dto)
            else -> null
        }
}
