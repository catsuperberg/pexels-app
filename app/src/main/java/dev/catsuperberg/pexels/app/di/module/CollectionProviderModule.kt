package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.domain.usecase.ICollectionProvider
import dev.catsuperberg.pexels.app.domain.usecase.PlaceholderCollectionProvider

@Module
@InstallIn(ViewModelComponent::class)
abstract class CollectionProviderModule {
    @Binds
    abstract fun bindCollectionProvider(collectionProvider: PlaceholderCollectionProvider): ICollectionProvider
}
