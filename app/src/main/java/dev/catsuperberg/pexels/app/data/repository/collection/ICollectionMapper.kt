package dev.catsuperberg.pexels.app.data.repository.collection

import dev.catsuperberg.pexels.app.data.caching.collection.CollectionEntity
import dev.catsuperberg.pexels.app.domain.model.PexelsCollection

interface ICollectionMapper {
    fun map(dto: CollectionDTO): PexelsCollection
    fun map(entity: CollectionEntity): PexelsCollection
    fun map(collection: PexelsCollection, creationTime: Long): CollectionEntity
}
