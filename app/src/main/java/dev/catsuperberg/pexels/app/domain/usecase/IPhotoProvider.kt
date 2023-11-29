package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.data.helper.DataSource
import dev.catsuperberg.pexels.app.data.helper.SourcedContainer
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

interface IPhotoProvider: ISinglePhotoProvider {
    suspend fun getCurated(page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>>
    suspend fun getSearch(query: String, page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>>
    suspend fun getCollection(id: String, page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>>
}

interface ISinglePhotoProvider {
    suspend fun getPhoto(id: Int): Result<SourcedContainer<PexelsPhoto, DataSource>>
}
