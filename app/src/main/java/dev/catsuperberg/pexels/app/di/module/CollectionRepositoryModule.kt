package dev.catsuperberg.pexels.app.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.data.caching.collection.CachedCollectionRepository
import dev.catsuperberg.pexels.app.data.caching.collection.CollectionDao
import dev.catsuperberg.pexels.app.data.repository.collection.CollectionApi
import dev.catsuperberg.pexels.app.data.repository.collection.CollectionRepository
import dev.catsuperberg.pexels.app.data.repository.collection.ICollectionMapper
import dev.catsuperberg.pexels.app.data.repository.collection.ICollectionRepository

@Module
@InstallIn(ViewModelComponent::class)
object CollectionRepositoryModule {
    @Provides
    fun providesCachedCollectionRepository(
        api: CollectionApi,
        mapper: ICollectionMapper,
        collectionDao: CollectionDao
    ): ICollectionRepository {
        val repository = CollectionRepository(api, mapper)
        return CachedCollectionRepository(repository, collectionDao, mapper)
    }
}
