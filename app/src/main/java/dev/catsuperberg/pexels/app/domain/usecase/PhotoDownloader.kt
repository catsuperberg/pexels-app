package dev.catsuperberg.pexels.app.domain.usecase

import dev.catsuperberg.pexels.app.data.storage.photo.IPhotoStorage
import javax.inject.Inject

class PhotoDownloader @Inject constructor(private val storage: IPhotoStorage) : IPhotoDownloader {
    override suspend fun download(sourceUrl: String): Result<Unit> = storage.saveToGallery(sourceUrl)
}
