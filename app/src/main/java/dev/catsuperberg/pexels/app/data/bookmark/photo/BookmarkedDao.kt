package dev.catsuperberg.pexels.app.data.bookmark.photo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarkedDao {
    @Query("SELECT EXISTS(SELECT * FROM bookmarked_photo WHERE id = :id)")
    fun isBookmarked(id: Int): Boolean

    @Query("SELECT * FROM bookmarked_photo WHERE id = :id")
    fun findById(id: Int): BookmarkedEntity

    @Query("SELECT * FROM bookmarked_photo ORDER BY timeAdded DESC LIMIT :perPage OFFSET ((:page - 1) * :perPage)")
    fun getPage(page: Int, perPage: Int): List<BookmarkedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg bookmarks: BookmarkedEntity)

    @Delete
    fun delete(bookmark: BookmarkedEntity)
}
