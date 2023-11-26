package dev.catsuperberg.pexels.app.data.storage.photo

interface IPhotoStorage {
    fun save(downloadUrl: String): String
    fun delete(localUri: String)
}
