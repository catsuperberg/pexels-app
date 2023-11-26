package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.domain.usecase.IPhotoDownloader
import dev.catsuperberg.pexels.app.domain.usecase.PhotoDownloader

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhotoDownloaderModule {
    @Binds
    abstract fun bindPhotoDownloader(photoMapper: PhotoDownloader): IPhotoDownloader
}
