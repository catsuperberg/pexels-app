package dev.catsuperberg.pexels.app.data.repository.collection

import dev.catsuperberg.pexels.app.domain.model.PexelsCollection
import javax.inject.Inject

class CollectionMapper @Inject constructor() : ICollectionMapper {
    override fun map(dto: CollectionDTO): PexelsCollection = PexelsCollection(dto.id, dto.title)
}
