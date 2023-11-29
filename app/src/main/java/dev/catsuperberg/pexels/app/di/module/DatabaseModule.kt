package dev.catsuperberg.pexels.app.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.catsuperberg.pexels.app.data.bookmark.photo.BookmarkedDao
import dev.catsuperberg.pexels.app.data.caching.collection.CollectionDao
import dev.catsuperberg.pexels.app.data.caching.photo.PhotoDao
import dev.catsuperberg.pexels.app.data.database.ApplicationDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext appContext: Context): ApplicationDatabase {
        return Room.databaseBuilder(
            appContext,
            ApplicationDatabase::class.java, "pexels-app-db"
        ).build()
    }

    @Singleton
    @Provides
    fun providesCollectionDao(db: ApplicationDatabase): CollectionDao = db.collectionDao()

    @Singleton
    @Provides
    fun providesBookmarkDao(db: ApplicationDatabase): BookmarkedDao = db.bookmarkedDao()

    @Singleton
    @Provides
    fun providesPhotoDao(db: ApplicationDatabase): PhotoDao = db.photoDao()
}
