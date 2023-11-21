package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.domain.usecase.CollectionProvider
import dev.catsuperberg.pexels.app.domain.usecase.ICollectionProvider

@Module
@InstallIn(ViewModelComponent::class)
abstract class CollectionProviderModule {
    @Binds
    abstract fun bindCollectionProvider(collectionProvider: CollectionProvider): ICollectionProvider
}
