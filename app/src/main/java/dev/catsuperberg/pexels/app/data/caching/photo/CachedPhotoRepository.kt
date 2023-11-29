package dev.catsuperberg.pexels.app.data.caching.photo

import android.util.Log
import dev.catsuperberg.pexels.app.data.caching.CacheConst
import dev.catsuperberg.pexels.app.data.database.PhotoEntity
import dev.catsuperberg.pexels.app.data.exception.DatabaseException.EmptyDatabaseResponseException
import dev.catsuperberg.pexels.app.data.exception.DatabaseException.FailedDatabaseRequestException
import dev.catsuperberg.pexels.app.data.helper.DataSource
import dev.catsuperberg.pexels.app.data.helper.SourcedContainer
import dev.catsuperberg.pexels.app.data.model.IPhotoMapper
import dev.catsuperberg.pexels.app.data.repository.photo.IPhotoRepository
import dev.catsuperberg.pexels.app.data.storage.photo.IPhotoStorage
import dev.catsuperberg.pexels.app.domain.model.PexelsPhoto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
annotation class CachedPhotoRepositoryImpl
class CachedPhotoRepository @Inject constructor(
    private val repository: IPhotoRepository,
    private val photoDao: PhotoDao,
    private val mapper: IPhotoMapper,
    private val storage: IPhotoStorage,
    private val sourcesToCache: Set<PhotoSource> = setOf()
): IPhotoRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val cacheLimit = 50

    override suspend fun getCurated(page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>> =
        withContext(Dispatchers.IO) {
            val result = repository.getCurated(page, perPage)
            if (sourcesToCache.contains(PhotoSource.CURATED).not())
                return@withContext result
            result.fold(
                onSuccess = {
                    launch { addDbPhotos(it.data, PhotoSource.CURATED) }
                    result
                },
                onFailure = { getFromDb(page, perPage, PhotoSource.CURATED) }
            )
        }

    override suspend fun getCollection(id: String, page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>> =
        withContext(Dispatchers.IO) {
            if (sourcesToCache.contains(PhotoSource.COLLECTION))
                throw NotImplementedError("Caching for ${PhotoSource.COLLECTION.name} not implemented: isn't in task requirements")
            repository.getCollection(id, page, perPage)
        }

    override suspend fun getSearch(query: String, page: Int, perPage: Int): Result<SourcedContainer<List<PexelsPhoto>, DataSource>> =
        withContext(Dispatchers.IO) {
            if (sourcesToCache.contains(PhotoSource.SEARCH))
                throw NotImplementedError("Caching for ${PhotoSource.SEARCH.name} not implemented: isn't in task requirements")
            repository.getSearch(query, page, perPage)
        }

    override suspend fun getPhoto(id: Int): Result<SourcedContainer<PexelsPhoto, DataSource>> = withContext(Dispatchers.IO) {
        if (sourcesToCache.contains(PhotoSource.ID))
            throw NotImplementedError("Caching for ${PhotoSource.ID.name} not implemented: isn't in task requirements")
        repository.getPhoto(id)
    }

    private fun addDbPhotos(photos: List<PexelsPhoto>, source: PhotoSource) {
        try {
            if (photoDao.getCount(source) >= cacheLimit)
                return

            val now = DateTime.now(DateTimeZone.UTC).millis
            val entities = photos.map { photo ->
                val localUriOptimized = storage.save(photo.urlOptimizedSize)
                mapper.map(
                    photo = photo,
                    localUriOriginal = localUriOptimized,
                    localUriOptimized = localUriOptimized,
                    cacheSource = source,
                    timeAdded = now,
                )
            }
            photoDao.insertAll(*entities.toTypedArray())
        } catch (e: Exception) {
            Log.e(this::class.toString(), "Failed updating photo cache db: $e")
        }
    }

    private fun getFromDb(page: Int, perPage: Int, source: PhotoSource): Result<SourcedContainer<List<PexelsPhoto>, DataSource>> {
        return try {
            val entities = clearExpired(photoDao.getPage(page, perPage, source))
            if (entities.isNotEmpty())
                Result.success(SourcedContainer(entities.map(mapper::map), DataSource.DATABASE))
            else
                Result.failure(EmptyDatabaseResponseException("No entries found for $source", null))
        } catch (e: Exception) {
            Result.failure(FailedDatabaseRequestException("Unexpected exception on database read", e))
        }
    }

    private fun clearExpired(entities: List<PhotoEntity>): List<PhotoEntity> {
        val expiredBefore = DateTime.now(DateTimeZone.UTC).minusSeconds(CacheConst.expirationSeconds).millis
        val (valid, expired) = entities.partition { it.timeAdded > expiredBefore }
        scope.launch { expired.forEach(photoDao::delete) }
        return valid
    }
}
