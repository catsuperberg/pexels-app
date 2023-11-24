package dev.catsuperberg.pexels.app.data.repository.photo

import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IPhotoMapper {
    fun map(dto: PhotoDTO): PexelsPhoto
    fun map(dto: Media): PexelsPhoto?
}
