package dev.catsuperberg.pexels.app.data.caching.photo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.catsuperberg.pexels.app.data.database.PhotoEntity

@Dao
interface PhotoDao {
    @Query("SELECT COUNT(id) FROM photo WHERE cacheSource = :source")
    fun getCount(source: PhotoSource): Int

    @Query("SELECT * FROM photo WHERE cacheSource = :source ORDER BY timeAdded DESC LIMIT :perPage OFFSET ((:page - 1) * :perPage)")
    fun getPage(page: Int, perPage: Int, source: PhotoSource): List<PhotoEntity>

    @Delete
    fun delete(photo: PhotoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg photos: PhotoEntity)

    @Query("DELETE FROM photo WHERE cacheSource = :source")
    fun deleteAll(source: PhotoSource)
}
