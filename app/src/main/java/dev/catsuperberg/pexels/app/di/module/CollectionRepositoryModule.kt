package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.data.repository.collection.CollectionRepository
import dev.catsuperberg.pexels.app.data.repository.collection.ICollectionRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class CollectionRepositoryModule {
    @Binds
    abstract fun bindCollectionRepository(collectionRepository: CollectionRepository): ICollectionRepository
}
