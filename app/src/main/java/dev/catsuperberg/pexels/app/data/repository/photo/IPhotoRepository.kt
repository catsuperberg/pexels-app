package dev.catsuperberg.pexels.app.data.repository.photo

import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IPhotoRepository: ISinglePhotoRepository {
    suspend fun getCurated(page: Int, perPage: Int): Result<List<PexelsPhoto>>
    suspend fun getCollection(id: String, page: Int, perPage: Int): Result<List<PexelsPhoto>>
    suspend fun getSearch(query: String, page: Int, perPage: Int): Result<List<PexelsPhoto>>
}

interface ISinglePhotoRepository {
    suspend fun getPhoto(id: Int): Result<PexelsPhoto>
}
