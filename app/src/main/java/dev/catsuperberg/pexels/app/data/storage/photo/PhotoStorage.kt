package dev.catsuperberg.pexels.app.data.storage.photo

import android.content.Context
import android.os.Environment
import dev.catsuperberg.pexels.app.data.exception.StorageException.FailedSavingFileException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.util.UUID
import javax.inject.Inject

class PhotoStorage @Inject constructor(private val context: Context) : IPhotoStorage {
    private val appName = context.getString(context.applicationInfo.labelRes)
    private val appDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName)

    override fun save(downloadUrl: String): String {
        val picture = URL(downloadUrl).readBytes()
        val file = File(context.filesDir, fileName())
        file.writeBytes(picture)
        return file.path
    }

    override fun delete(localUri: String) {
        val file = File(localUri)
        file.delete()
    }

    override suspend fun saveToGallery(downloadUrl: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (appDir.exists().not())
                appDir.mkdirs()

            val localFile = File(downloadUrl)
            val picture = if (localFile.exists()) localFile.readBytes() else URL(downloadUrl).readBytes()
            val file = File(appDir, fileName())
            file.writeBytes(picture)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(FailedSavingFileException("Couldn't save file. Cause: $e"))
        }
    }

    private fun fileName(): String {
        return "${UUID.randomUUID()}.jpg"
    }
}
