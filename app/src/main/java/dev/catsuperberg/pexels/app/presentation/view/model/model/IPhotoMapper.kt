package dev.catsuperberg.pexels.app.presentation.view.model.model

import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto

enum class PhotoQuality { ORIGINAL, OPTIMIZED }

interface IPhotoMapper {
    fun map(photo: PexelsPhoto, quality: PhotoQuality = PhotoQuality.OPTIMIZED): Photo
}
