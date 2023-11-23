package dev.catsuperberg.pexels.app.data.repository.collection

import dev.catsuperberg.pexels.app.data.caching.collection.CollectionEntity
import dev.catsuperberg.pexels.app.domain.model.PexelsCollection
import javax.inject.Inject

class CollectionMapper @Inject constructor() : ICollectionMapper {
    override fun map(dto: CollectionDTO): PexelsCollection =
        PexelsCollection(dto.id, dto.title)

    override fun map(entity: CollectionEntity): PexelsCollection =
        PexelsCollection(entity.id, entity.title)

    override fun map(collection: PexelsCollection, creationTime: Long): CollectionEntity =
        CollectionEntity(collection.id, collection.title, creationTime)
}
