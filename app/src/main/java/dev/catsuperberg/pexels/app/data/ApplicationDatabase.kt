package dev.catsuperberg.pexels.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.catsuperberg.pexels.app.data.caching.collection.CollectionDao
import dev.catsuperberg.pexels.app.data.caching.collection.CollectionEntity

@Database(entities = [CollectionEntity::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
}
