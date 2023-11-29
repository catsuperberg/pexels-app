package dev.catsuperberg.pexels.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.catsuperberg.pexels.app.data.bookmark.photo.BookmarkedDao
import dev.catsuperberg.pexels.app.data.caching.collection.CollectionDao
import dev.catsuperberg.pexels.app.data.caching.collection.CollectionEntity
import dev.catsuperberg.pexels.app.data.caching.photo.PhotoDao

@Database(entities = [CollectionEntity::class, PhotoEntity::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
    abstract fun bookmarkedDao(): BookmarkedDao
    abstract fun photoDao(): PhotoDao
}
