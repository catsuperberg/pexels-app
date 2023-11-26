package dev.catsuperberg.pexels.app.domain.usecase

interface IBookmarkAccess {
    suspend fun isBookmarked(photoId: Int): Result<Boolean>
    suspend fun setBookmarked(photoId: Int, state: Boolean): Result<Unit>
}
