package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.data.model.IPhotoMapper
import dev.catsuperberg.pexels.app.data.model.PhotoMapper

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhotoMapperModule {
    @Binds
    abstract fun bindPhotoMapper(photoMapper: PhotoMapper): IPhotoMapper
}
