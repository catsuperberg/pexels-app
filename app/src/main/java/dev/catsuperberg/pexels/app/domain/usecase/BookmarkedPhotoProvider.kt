package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.data.bookmark.photo.IBookmarkedPhotoRepository
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import javax.inject.Inject

class BookmarkedPhotoProvider @Inject constructor(
    private val repository: IBookmarkedPhotoRepository
) : IBookmarkedPhotoProvider {
    override suspend fun getPhotos(page: Int, perPage: Int): Result<List<PexelsPhoto>> =
        repository.getBookmarked(page, perPage)
}
