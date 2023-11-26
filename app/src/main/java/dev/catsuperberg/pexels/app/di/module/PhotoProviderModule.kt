package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.domain.usecase.IPhotoProvider
import dev.catsuperberg.pexels.app.domain.usecase.ISinglePhotoProvider
import dev.catsuperberg.pexels.app.domain.usecase.PhotoProvider

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhotoProviderModule {
    @Binds
    abstract fun bindPhotoProvider(photoProvider: PhotoProvider): IPhotoProvider

    @Binds
    abstract fun bindSinglePhotoProvider(photoProvider: PhotoProvider): ISinglePhotoProvider
}
