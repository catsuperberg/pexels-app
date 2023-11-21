package dev.catsuperberg.pexels.app.data.repository.collection

import dev.catsuperberg.pexels.app.domain.model.PexelsCollection

interface ICollectionMapper {
    fun map(dto: CollectionDTO): PexelsCollection
}
