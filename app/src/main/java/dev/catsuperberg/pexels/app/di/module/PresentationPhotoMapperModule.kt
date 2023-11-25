package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.presentation.view.model.model.IPhotoMapper
import dev.catsuperberg.pexels.app.presentation.view.model.model.PhotoMapper

@Module
@InstallIn(ViewModelComponent::class)
abstract class PresentationPhotoMapperModule {
    @Binds
    abstract fun bindPresentationPhotoMapper(photoMapper: PhotoMapper): IPhotoMapper
}
