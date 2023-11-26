package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.data.model.CollectionMapper
import dev.catsuperberg.pexels.app.data.model.ICollectionMapper

@Module
@InstallIn(ViewModelComponent::class)
abstract class CollectionMapperModule {
    @Binds
    abstract fun bindCollectionMapper(collectionMapper: CollectionMapper): ICollectionMapper
}
