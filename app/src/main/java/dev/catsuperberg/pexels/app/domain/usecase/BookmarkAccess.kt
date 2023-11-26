package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.data.bookmark.photo.IBookmarkedPhotoRepository
import javax.inject.Inject

class BookmarkAccess @Inject constructor(
    private val repository: IBookmarkedPhotoRepository
) : IBookmarkAccess {
    override suspend fun isBookmarked(photoId: Int): Result<Boolean> = repository.isBookmarked(photoId)

    override suspend fun setBookmarked(photoId: Int, state: Boolean): Result<Unit> {
        return if (state) repository.saveBookmarked(photoId) else repository.delete(photoId)
    }
}
