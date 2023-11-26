package dev.catsuperberg.pexels.app.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.domain.usecase.BookmarkAccess
import dev.catsuperberg.pexels.app.domain.usecase.IBookmarkAccess

@Module
@InstallIn(ViewModelComponent::class)
abstract class BookmarkAccessModule {
    @Binds
    abstract fun bindBookmarkAccess(bookmarkAccess: BookmarkAccess): IBookmarkAccess
}
