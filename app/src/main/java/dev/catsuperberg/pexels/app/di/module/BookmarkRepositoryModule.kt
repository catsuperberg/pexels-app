package dev.catsuperberg.pexels.app.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.data.bookmark.photo.BookmarkedDao
import dev.catsuperberg.pexels.app.data.bookmark.photo.BookmarkedPhotoRepository
import dev.catsuperberg.pexels.app.data.bookmark.photo.IBookmarkedPhotoRepository
import dev.catsuperberg.pexels.app.data.model.IPhotoMapper
import dev.catsuperberg.pexels.app.data.repository.photo.IPhotoRepository
import dev.catsuperberg.pexels.app.data.storage.photo.IPhotoStorage

@Module
@InstallIn(ViewModelComponent::class)
object BookmarkRepositoryModule {
    @Provides
    fun providesBookmarkedPhotoRepository(
        repository: IPhotoRepository,
        bookmarkedDao: BookmarkedDao,
        mapper: IPhotoMapper,
        storage: IPhotoStorage
    ): IBookmarkedPhotoRepository {
        return BookmarkedPhotoRepository(repository, bookmarkedDao, mapper, storage)
    }
}
