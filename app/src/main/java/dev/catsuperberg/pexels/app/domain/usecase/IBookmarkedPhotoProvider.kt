package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IBookmarkedPhotoProvider {
    suspend fun getPhotos(page: Int, perPage: Int): Result<List<PexelsPhoto>>
}
