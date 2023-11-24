package dev.catsuperberg.pexels.app.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.catsuperberg.pexels.app.data.repository.photo.IPhotoMapper
import dev.catsuperberg.pexels.app.data.repository.photo.IPhotoRepository
import dev.catsuperberg.pexels.app.data.repository.photo.PhotoRepository
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
}
