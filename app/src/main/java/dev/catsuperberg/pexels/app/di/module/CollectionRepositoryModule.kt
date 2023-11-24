package dev.catsuperberg.pexels.app.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.data.caching.collection.CachedCollectionRepository
import dev.catsuperberg.pexels.app.data.caching.collection.CollectionDao
import dev.catsuperberg.pexels.app.data.repository.collection.CollectionRepository
import dev.catsuperberg.pexels.app.data.repository.collection.ICollectionMapper
import dev.catsuperberg.pexels.app.data.repository.collection.ICollectionRepository
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object CollectionRepositoryModule {
    @Provides
    fun providesCachedCollectionRepository(
        client: Retrofit,
        mapper: ICollectionMapper,
        collectionDao: CollectionDao
    ): ICollectionRepository {
        val repository = CollectionRepository(client, mapper)
        return CachedCollectionRepository(repository, collectionDao, mapper)
    }
}
