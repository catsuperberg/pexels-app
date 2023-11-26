package dev.catsuperberg.pexels.app.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.catsuperberg.pexels.app.data.storage.photo.IPhotoStorage
import dev.catsuperberg.pexels.app.data.storage.photo.PhotoStorage

@Module
@InstallIn(SingletonComponent::class)
object PhotoStorageModule {
    @Provides
     fun providesPhotoStorage(@ApplicationContext appContext: Context): IPhotoStorage = PhotoStorage(appContext)
}
