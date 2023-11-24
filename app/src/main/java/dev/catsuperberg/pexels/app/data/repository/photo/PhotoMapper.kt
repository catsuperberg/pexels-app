package dev.catsuperberg.pexels.app.data.repository.photo

import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import javax.inject.Inject

class PhotoMapper @Inject constructor()  : IPhotoMapper {
    override fun map(dto: PhotoDTO): PexelsPhoto =
        PexelsPhoto(
            dto.id,
            dto.photographer,
            dto.src.original,
            dto.alt.ifEmpty { null }
        )

    override fun map(dto: Media): PexelsPhoto? = when(dto) {
            is PhotoDTO -> map(dto)
            else -> null
        }
}
