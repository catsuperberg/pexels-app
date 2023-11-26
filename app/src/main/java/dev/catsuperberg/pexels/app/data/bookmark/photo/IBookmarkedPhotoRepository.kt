package dev.catsuperberg.pexels.app.data.bookmark.photo

import dev.catsuperberg.pexels.app.data.repository.photo.ISinglePhotoRepository
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IBookmarkedPhotoRepository: ISinglePhotoRepository {
    suspend fun isBookmarked(id: Int): Result<Boolean>
    suspend fun getBookmarked(page: Int, perPage: Int): Result<List<PexelsPhoto>>
    suspend fun saveBookmarked(id: Int): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
}
