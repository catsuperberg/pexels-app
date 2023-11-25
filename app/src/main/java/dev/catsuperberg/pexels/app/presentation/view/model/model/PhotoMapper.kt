package dev.catsuperberg.pexels.app.presentation.view.model.model

import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import javax.inject.Inject

class PhotoMapper @Inject constructor() : IPhotoMapper {
    override fun map(photo: PexelsPhoto, quality: PhotoQuality): Photo = Photo(
        url = photo.urlOptimizedSize,
        aspectRatio = photo.width.toFloat() / photo.height.toFloat(),
        author = photo.photographer,
        description = photo.alt
    )
}
