package dev.catsuperberg.pexels.app.data.caching.collection

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collection")
    fun getAll(): List<CollectionEntity>

    @Query("SELECT * FROM collection WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<CollectionEntity>

    @Query("SELECT * FROM collection LIMIT :count")
    fun getFirst(count: Int): List<CollectionEntity>

    @Query("SELECT * FROM collection WHERE title = :title")
    fun findByTitle(title: String): CollectionEntity

    @Insert
    fun insertAll(vararg users: CollectionEntity)

    @Delete
    fun delete(collection: CollectionEntity)

    @Query("DELETE FROM collection")
    fun deleteAll()
}
