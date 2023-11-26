package dev.catsuperberg.pexels.app.domain.usecase

interface IPhotoDownloader {
    suspend fun download(sourceUrl: String): Result<Unit>
}
