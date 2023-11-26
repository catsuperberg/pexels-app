package dev.catsuperberg.pexels.app.data.storage.photo

import android.content.Context
import java.io.File
import java.net.URL
import java.util.UUID
import javax.inject.Inject

class PhotoStorage @Inject constructor(private val context: Context) : IPhotoStorage {
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

    private fun fileName(): String {
        return "${UUID.randomUUID()}.jpg"
    }
}
