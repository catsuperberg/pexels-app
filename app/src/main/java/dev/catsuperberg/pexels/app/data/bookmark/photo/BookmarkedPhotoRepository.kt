package dev.catsuperberg.pexels.app.data.bookmark.photo

import dev.catsuperberg.pexels.app.data.exception.DatabaseException.FailedDatabaseDeleteException
import dev.catsuperberg.pexels.app.data.exception.DatabaseException.FailedDatabaseInsertException
import dev.catsuperberg.pexels.app.data.exception.DatabaseException.FailedDatabaseRequestException
import dev.catsuperberg.pexels.app.data.model.IPhotoMapper
import dev.catsuperberg.pexels.app.data.repository.photo.ISinglePhotoRepository
import dev.catsuperberg.pexels.app.data.storage.photo.IPhotoStorage
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import javax.inject.Inject

class BookmarkedPhotoRepository @Inject constructor(
    private val repository: ISinglePhotoRepository,
    private val bookmarkedDao: BookmarkedDao,
    private val mapper: IPhotoMapper,
    private val storage: IPhotoStorage
): IBookmarkedPhotoRepository {
    override suspend fun isBookmarked(id: Int): Result<Boolean> = accessDb { bookmarkedDao.isBookmarked(id) }

    override suspend fun getBookmarked(page: Int, perPage: Int): Result<List<PexelsPhoto>> = accessDb {
        bookmarkedDao.getPage(page, perPage).map(mapper::map)
    }

    override suspend fun saveBookmarked(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
             repository.getPhoto(id).fold(
                onSuccess = { photo ->
                    val now = DateTime.now(DateTimeZone.UTC).millis
                    val localUriOriginal = storage.save(photo.urlOriginalSize)
                    val localUriOptimized = storage.save(photo.urlOptimizedSize)
                    bookmarkedDao.insertAll(
                        mapper.map(
                            photo = photo,
                            localUriOriginal = localUriOriginal,
                            localUriOptimized = localUriOptimized,
                            bookmarked = true,
                            timeAdded = now,
                        )
                    )
                    Result.success(Unit)
                },
                onFailure = { e -> throw e }
            )
        } catch(e: Exception) {
            Result.failure(FailedDatabaseInsertException("Couldn't save bookmarked photo: $id. Cause: $e"))
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val photo = bookmarkedDao.findById(id)
            storage.delete(photo.localUriOriginal)
            storage.delete(photo.localUriOptimized)
            bookmarkedDao.delete(photo)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(FailedDatabaseDeleteException("Couldn't delete bookmarked photo: $id. Cause: $e"))
        }
    }

    override suspend fun getPhoto(id: Int): Result<PexelsPhoto> = accessDb { mapper.map(bookmarkedDao.findById(id)) }

    private suspend fun <T> accessDb(action: () -> T): Result<T> = withContext(Dispatchers.IO) {
        try {
            Result.success(action())
        } catch (e: Exception) {
            Result.failure(FailedDatabaseRequestException(e.toString()))
        }
    }
}
