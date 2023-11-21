package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.data.repository.collection.CollectionMapper
import dev.catsuperberg.pexels.app.data.repository.collection.ICollectionMapper

@Module
@InstallIn(ViewModelComponent::class)
abstract class CollectionMapperModule {
    @Binds
    abstract fun bindCollectionMapper(collectionMapper: CollectionMapper): ICollectionMapper
}
