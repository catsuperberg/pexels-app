package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IPhotoProvider {
    suspend fun getCurated(count: Int): Result<List<PexelsPhoto>>
    suspend fun getSearch(query: String, count: Int): Result<List<PexelsPhoto>>
    suspend fun getCollection(id: String, count: Int): Result<List<PexelsPhoto>>
    suspend fun getPhoto(id: Int): Result<PexelsPhoto>
}
