package dev.catsuperberg.pexels.app.di.module

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.catsuperberg.pexels.app.data.bookmark.photo.IBookmarkedPhotoRepository
import dev.catsuperberg.pexels.app.data.repository.photo.IPhotoRepository
import dev.catsuperberg.pexels.app.domain.usecase.IPhotoProvider
import dev.catsuperberg.pexels.app.domain.usecase.ISinglePhotoProvider
import dev.catsuperberg.pexels.app.domain.usecase.PhotoProvider
import dev.catsuperberg.pexels.app.domain.usecase.SinglePhotoProvider
import dev.catsuperberg.pexels.app.presentation.ui.navArgs
import dev.catsuperberg.pexels.app.presentation.view.model.DetailsScreenNavArgs

@Module
@InstallIn(ViewModelComponent::class)
object SinglePhotoProviderModule {
    @Provides
    @ViewModelScoped
    fun providesPhotoProvider(repository: IPhotoRepository): IPhotoProvider {
        return PhotoProvider(repository, SinglePhotoProvider(repository))
    }

    @Provides
    @ViewModelScoped
    fun providesSinglePhotoProvider(
        stateHandle: SavedStateHandle,
        bookmarkedRepository: IBookmarkedPhotoRepository,
        repository: IPhotoRepository
    ): ISinglePhotoProvider {
        val storageOnly = stateHandle.navArgs<DetailsScreenNavArgs>().storageOnlyProvider
        return if (storageOnly) SinglePhotoProvider(bookmarkedRepository) else SinglePhotoProvider(repository)
    }
}
