package dev.catsuperberg.pexels.app.domain.model

data class PexelsPhoto(
    val id: Int,
    val width: Int,
    val height: Int,
    val photographer: String,
    val urlOriginalSize: String,
    val urlOptimizedSize: String,
    val alt: String?
)
