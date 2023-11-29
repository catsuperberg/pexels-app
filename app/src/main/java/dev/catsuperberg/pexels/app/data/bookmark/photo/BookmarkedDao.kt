package dev.catsuperberg.pexels.app.data.bookmark.photo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.catsuperberg.pexels.app.data.database.PhotoEntity

@Dao
interface BookmarkedDao {
    @Query("SELECT EXISTS(SELECT * FROM photo WHERE bookmarked = 1 AND id = :id )")
    fun isBookmarked(id: Int): Boolean

    @Query("SELECT * FROM photo WHERE bookmarked = 1 AND id = :id")
    fun findById(id: Int): PhotoEntity

    @Query("SELECT * FROM photo WHERE bookmarked = 1 ORDER BY timeAdded DESC LIMIT :perPage OFFSET ((:page - 1) * :perPage)")
    fun getPage(page: Int, perPage: Int): List<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg bookmarks: PhotoEntity)

    @Delete
    fun delete(bookmark: PhotoEntity)
}
