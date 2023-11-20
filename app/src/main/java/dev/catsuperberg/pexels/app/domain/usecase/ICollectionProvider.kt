package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.domain.model.PexelsCollection

interface ICollectionProvider {
    suspend fun get(count: Int): Result<List<PexelsCollection>>
}
