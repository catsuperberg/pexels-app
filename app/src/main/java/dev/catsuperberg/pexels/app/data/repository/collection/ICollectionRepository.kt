package dev.catsuperberg.pexels.app.data.repository.collection

import dev.catsuperberg.pexels.app.domain.model.PexelsCollection

interface ICollectionRepository {
    suspend fun getFeatured(count: Int): Result<List<PexelsCollection>>
}
