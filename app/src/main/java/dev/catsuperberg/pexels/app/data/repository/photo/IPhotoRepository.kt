package dev.catsuperberg.pexels.app.data.repository.photo

import dev.catsuperberg.pexels.app.data.helper.DataSource
import dev.catsuperberg.pexels.app.data.helper.SourcedContainer
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IPhotoRepository: ISinglePhotoRepository {
    suspend fun getCurated(page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>>
    suspend fun getCollection(id: String, page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>>
    suspend fun getSearch(query: String, page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>>
}

interface ISinglePhotoRepository {
    suspend fun getPhoto(id: Int): Result<SourcedContainer<PexelsPhoto, DataSource>>
}
