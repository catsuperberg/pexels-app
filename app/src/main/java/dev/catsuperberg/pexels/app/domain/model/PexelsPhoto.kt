package dev.catsuperberg.pexels.app.domain.model

data class PexelsPhoto(
    val id: Int,
    val photographer: String,
    val url: String,
    val alt: String?
)
