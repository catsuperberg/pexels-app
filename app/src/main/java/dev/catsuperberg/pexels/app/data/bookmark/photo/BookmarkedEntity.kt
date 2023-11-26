package dev.catsuperberg.pexels.app.data.bookmark.photo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("bookmarked_photo")
data class BookmarkedEntity(
    @PrimaryKey val id: Int,
    val width: Int,
    val height: Int,
    val photographer: String,
    val localUriOriginal: String,
    val localUriOptimized: String,
    val alt: String?,
    val timeAdded: Long,
)
