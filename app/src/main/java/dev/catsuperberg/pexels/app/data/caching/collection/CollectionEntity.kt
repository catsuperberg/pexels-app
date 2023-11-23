package dev.catsuperberg.pexels.app.data.caching.collection

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("collection")
data class CollectionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val timeCreated: Long
)
