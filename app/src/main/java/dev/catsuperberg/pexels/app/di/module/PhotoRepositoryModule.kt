package dev.catsuperberg.pexels.app.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.data.caching.photo.CachedPhotoRepository
import dev.catsuperberg.pexels.app.data.caching.photo.CachedPhotoRepositoryImpl
import dev.catsuperberg.pexels.app.data.caching.photo.PhotoDao
import dev.catsuperberg.pexels.app.data.caching.photo.PhotoSource
import dev.catsuperberg.pexels.app.data.model.IPhotoMapper
import dev.catsuperberg.pexels.app.data.repository.photo.IPhotoRepository
import dev.catsuperberg.pexels.app.data.repository.photo.PhotoRepository
import dev.catsuperberg.pexels.app.data.storage.photo.IPhotoStorage
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object PhotoRepositoryModule {
    @Provides
    fun providesPhotoRepository(
        client: Retrofit,
        mapper: IPhotoMapper,
    ): IPhotoRepository {
        return PhotoRepository(client, mapper)
    }


    @Provides
    @CachedPhotoRepositoryImpl
    fun providesCachedPhotoRepository(
        client: Retrofit,
        photoDao: PhotoDao,
        mapper: IPhotoMapper,
        storage: IPhotoStorage,
    ): IPhotoRepository {
        val repository = PhotoRepository(client, mapper)
        val cachedSources = setOf(PhotoSource.CURATED)
        return CachedPhotoRepository(repository, photoDao, mapper, storage, cachedSources)
    }
}
