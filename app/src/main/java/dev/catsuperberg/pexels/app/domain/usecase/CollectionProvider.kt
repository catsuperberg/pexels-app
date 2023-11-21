package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.data.repository.collection.ICollectionRepository
import dev.catsuperberg.pexels.app.domain.model.PexelsCollection
import javax.inject.Inject

class CollectionProvider @Inject constructor(
    private val repository: ICollectionRepository
) : ICollectionProvider {

    override suspend fun get(count: Int): Result<List<PexelsCollection>> {
        return repository.getFeatured(count)
    }
}
