package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.data.repository.photo.IPhotoRepository
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import javax.inject.Inject

class PhotoProvider @Inject constructor(
    private val repository: IPhotoRepository
) : IPhotoProvider {
    override suspend fun getCurated(count: Int): Result<List<PexelsPhoto>> {
        return repository.getCurated(1, count)
    }

    override suspend fun getSearch(query: String, count: Int): Result<List<PexelsPhoto>> {
        return repository.getSearch(query, 1, count)
    }

    override suspend fun getCollection(id: String, count: Int): Result<List<PexelsPhoto>> {
        return repository.getCollection(id, 1, count)
    }
}
