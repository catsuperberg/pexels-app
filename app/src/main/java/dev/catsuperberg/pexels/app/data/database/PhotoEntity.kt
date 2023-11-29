package dev.catsuperberg.pexels.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.catsuperberg.pexels.app.data.caching.photo.PhotoSource

@Entity("photo")
data class PhotoEntity(
    @PrimaryKey val id: Int,
    val width: Int,
    val height: Int,
    val photographer: String,
    val localUriOriginal: String,
    val localUriOptimized: String,
    val alt: String?,
    val bookmarked: Boolean,
    val cacheSource: PhotoSource?,
    val timeAdded: Long,
)
