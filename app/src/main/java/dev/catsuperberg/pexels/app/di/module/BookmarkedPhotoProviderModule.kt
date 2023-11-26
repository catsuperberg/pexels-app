package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.domain.usecase.BookmarkedPhotoProvider
import dev.catsuperberg.pexels.app.domain.usecase.IBookmarkedPhotoProvider

@Module
@InstallIn(ViewModelComponent::class)
abstract class BookmarkedPhotoProviderModule {
    @Binds
    abstract fun bindBookmarkPhotoProvider(bookmarkedPhotoProvider: BookmarkedPhotoProvider): IBookmarkedPhotoProvider
}
