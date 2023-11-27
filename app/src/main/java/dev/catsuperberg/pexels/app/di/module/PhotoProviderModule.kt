package dev.catsuperberg.pexels.app.di.module

import androidx.lifecycle.SavedStateHandle
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.catsuperberg.pexels.app.data.bookmark.photo.IBookmarkedPhotoRepository
import dev.catsuperberg.pexels.app.data.repository.photo.PhotoRepository
import dev.catsuperberg.pexels.app.domain.usecase.IPhotoProvider
import dev.catsuperberg.pexels.app.domain.usecase.ISinglePhotoProvider
import dev.catsuperberg.pexels.app.domain.usecase.PhotoProvider
import dev.catsuperberg.pexels.app.domain.usecase.SinglePhotoProvider
import dev.catsuperberg.pexels.app.presentation.ui.navArgs
import dev.catsuperberg.pexels.app.presentation.view.model.DetailsScreenNavArgs

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhotoProviderModule {
    @Binds
    abstract fun bindPhotoProvider(photoProvider: PhotoProvider): IPhotoProvider
}

@Module
@InstallIn(ViewModelComponent::class)
object SinglePhotoProviderModule {
    @Provides
    @ViewModelScoped
    fun providesSinglePhotoProvider(
        stateHandle: SavedStateHandle,
        bookmarkedRepository: IBookmarkedPhotoRepository,
        repository: PhotoRepository
    ): ISinglePhotoProvider {
        val storageOnly = stateHandle.navArgs<DetailsScreenNavArgs>().storageOnlyProvider
        return if (storageOnly) SinglePhotoProvider(bookmarkedRepository) else SinglePhotoProvider(repository)
    }
}
