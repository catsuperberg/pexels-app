package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IPhotoProvider: ISinglePhotoProvider {
    suspend fun getCurated(page: Int, perPage: Int): Result<List<PexelsPhoto>>
    suspend fun getSearch(query: String, page: Int, perPage: Int): Result<List<PexelsPhoto>>
    suspend fun getCollection(id: String, page: Int, perPage: Int): Result<List<PexelsPhoto>>
}

interface ISinglePhotoProvider {
    suspend fun getPhoto(id: Int): Result<PexelsPhoto>
}
