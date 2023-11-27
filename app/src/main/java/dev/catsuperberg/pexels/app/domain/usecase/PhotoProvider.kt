package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.data.repository.photo.IPhotoRepository
import dev.catsuperberg.pexels.app.data.repository.photo.ISinglePhotoRepository
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import javax.inject.Inject

class PhotoProvider @Inject constructor(
    private val repository: IPhotoRepository,
    private val singlePhotoProvider: ISinglePhotoProvider
) : IPhotoProvider, ISinglePhotoProvider by singlePhotoProvider {
    override suspend fun getCurated(page: Int, perPage: Int): Result<List<PexelsPhoto>> {
        return repository.getCurated(page, perPage)
    }

    override suspend fun getSearch(query: String, count: Int): Result<List<PexelsPhoto>> {
        return repository.getSearch(query, 1, count)
    }

    override suspend fun getCollection(id: String, count: Int): Result<List<PexelsPhoto>> {
        return repository.getCollection(id, 1, count)
    }
}

class SinglePhotoProvider @Inject constructor(
    private val repository: ISinglePhotoRepository
) : ISinglePhotoProvider {
    override suspend fun getPhoto(id: Int): Result<PexelsPhoto> = repository.getPhoto(id)
}
