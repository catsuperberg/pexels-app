package dev.catsuperberg.pexels.app.data.model

import dev.catsuperberg.pexels.app.data.bookmark.photo.BookmarkedEntity
import dev.catsuperberg.pexels.app.data.repository.photo.Media
import dev.catsuperberg.pexels.app.data.repository.photo.PhotoDTO
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IPhotoMapper {
    fun map(dto: PhotoDTO): PexelsPhoto
    fun map(dto: Media): PexelsPhoto?
    fun map(entity: BookmarkedEntity): PexelsPhoto
    fun map(photo: PexelsPhoto, localUriOriginal: String, localUriOptimized: String, timeAdded: Long): BookmarkedEntity
}
