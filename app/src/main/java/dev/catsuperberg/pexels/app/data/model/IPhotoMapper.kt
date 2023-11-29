package dev.catsuperberg.pexels.app.data.model

import dev.catsuperberg.pexels.app.data.database.PhotoEntity
import dev.catsuperberg.pexels.app.data.repository.photo.Media
import dev.catsuperberg.pexels.app.data.repository.photo.PhotoDTO
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IPhotoMapper {
    fun map(dto: PhotoDTO): PexelsPhoto
    fun map(dto: Media): PexelsPhoto?
    fun map(entity: PhotoEntity): PexelsPhoto
    fun map(
        photo: PexelsPhoto,
        localUriOriginal: String,
        localUriOptimized: String,
        bookmarked: Boolean = false,
        cached: Boolean = false,
        timeAdded: Long
    ): PhotoEntity
}
